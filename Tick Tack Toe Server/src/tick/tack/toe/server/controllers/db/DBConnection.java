/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.controllers.db;

import java.net.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import tick.tack.toe.server.controllers.client.ClientHandler;

import tick.tack.toe.server.models.*;

/**
 *
 * @author wwwmo
 */
public class DBConnection {

//    private final String dbName = "u635309332_Tick_Tack_Toe";
//    private final String dbHost = "109.106.246.1";
//    private final String dbPort = "3306";
//    private final String dbUser = "u635309332_itiTeam";
//    private final String dbPass = "Itians:0)";
    
    private final String dbName = "u635309332_Tick_Tack_Toe";
    private final String dbHost = "localhost";
    private final String dbPort = "3306";
    private final String dbUser = "root";
    private final String dbPass = "1234";
    private static Connection connection = null;
    
    static Cipher cipher; 
    SecretKey secretKey = decodeKeyFromString("hXarwfB0G/KydnnYeJOhcw==");
    KeyGenerator keyGenerator;

    public DBConnection() {
        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?autoReconnect=true", dbUser, dbPass);
            System.out.println("connected To DB...");
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("You are Offline!");
        }
    }

    private void closeDataBaseConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }

    /**
     * Encryption and Decryption
     */
    
    public static String encrypt(String plainText, SecretKey secretKey) {
        byte[] plainTextByte = plainText.getBytes();
        try {
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedByte = cipher.doFinal(plainTextByte);
            Base64.Encoder encoder = Base64.getEncoder();
            String encryptedText = encoder.encodeToString(encryptedByte);
            return encryptedText;
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            System.out.println("Some thing related to Encryption Happened");
            return plainText;
        } 
    }

    public static String keyToString(SecretKey secretKey) {
        /* Get key in encoding format */
        byte encoded[] = secretKey.getEncoded();

        /*
         * Encodes the specified byte array into a String using Base64 encoding
         * scheme
         */
        String encodedKey = Base64.getEncoder().encodeToString(encoded);

        return encodedKey;
    }
    
    public static SecretKey decodeKeyFromString(String keyStr) {
        /* Decodes a Base64 encoded String into a byte array */
        byte[] decodedKey = Base64.getDecoder().decode(keyStr);

        /* Constructs a secret key from the given byte array */
        SecretKey secretKey = new SecretKeySpec(decodedKey, 0,
          decodedKey.length, "AES");

        return secretKey;
    }
    /**
     * ******** Start Sign In **************
     */
    public int authenticate(Credentials credentials) {
        try {
            System.out.println("Auth satr 1");
            Statement passStmt = connection.createStatement();
            ResultSet pass = passStmt.executeQuery("select id, password from player where username='"+ credentials.getUsername() +"' LIMIT 1");
            
            pass.next();
            
            if (encrypt( credentials.getPassword(), secretKey).equals(pass.getString("password"))){
                return pass.getInt("id");
            } else {
                System.out.println("encrypt( credentials.getPassword(), decodeKeyFromString(passAndKey.getString(\"encKey\"))): "+ encrypt( credentials.getPassword(), secretKey));
                System.out.println("passAndKey.getString(\"password\"): "+ pass.getString("password"));
                System.out.println("Hello from Encryption");
            }
           
        } catch (SQLException e) {

            //try { Thread.sleep(500); } catch (InterruptedException ignored) { }
                //authenticate(credentials);

            System.out.println(e);
        }


        System.out.println("Auth satr 5");
        return -1;
    }
    public int getTheLastPlayerId() {
        try {
            Statement stmt = connection.createStatement();
            String queryString = new String("select id from player ORDER BY id DESC LIMIT 1");
            ResultSet rs = stmt.executeQuery(queryString);
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {

            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            getTheLastPlayerId();

            System.out.println(e);
        }


        System.out.println("Auth satr 5");
        return -1;
    }
    public void updatePlayerDBStatus(int player_id, String state) {
        PreparedStatement p = null;
        try {
            p = connection.prepareStatement("update player set status= ? where id = ?");
            p.setString(1, state);
            p.setInt(2, player_id);
            p.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * ******** End Sign In **************
     */
    /**
     * ******** Start Sign Up And Return PlayerFullInfo model **************
     */
    public PlayerFullInfo signUp(User user) {
        
         try {
//            keyGenerator = KeyGenerator.getInstance("AES");
//            keyGenerator.init(128); // block size is 128bits
//            secretKey = keyGenerator.generateKey();
            cipher = Cipher.getInstance("AES"); //SunJCE provider AES algorithm, mode(optional) and padding schema(optional)  
        } catch (NoSuchAlgorithmException | NoSuchPaddingException ex) {
            System.out.println("Error While Creating Key for Ecryption!!");
        }
        
        String encryptedText = encrypt(user.getPassword(), secretKey);          
        
        if (validateUser(user.getUsername())) {
            return null;
        } else {
            try {
                System.out.println("1: "+  user.getUsername());
                System.out.println("2 "+  user.getName());
                System.out.println("3: "+  user.getPassword());
                PreparedStatement stm = connection.prepareStatement("insert into player (username, user, password) values (?,?,?);");
                stm.setString(1, user.getUsername());
                stm.setString(2, user.getName());
                stm.setString(3, encryptedText);
                System.out.println("hello DBConnection Class -> SignUp Method: "+stm.toString());
                stm.executeUpdate();
                System.out.println("Good Job");
                return getPlayerInfo(user.getUsername());
            } catch (SQLException ex) {
//                try { Thread.sleep(500); } catch (InterruptedException ignored) { }
//                signUp(user);       
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        return null;
    }

    // -> Validate User Existance
    public boolean validateUser(String username) {
        try {
            System.out.println("user: "+username);
            PreparedStatement stm = connection.prepareStatement("select * from player where username=?");
            stm.setString(1, username);
            ResultSet result = stm.executeQuery();
            return result.next();
        } catch (SQLException ex) {
//            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
//            validateUser(username); 
            System.out.println(ex);
            ex.printStackTrace();
        }
        return false;
    }

    private PlayerFullInfo getPlayerInfo(String username) {
        try {
            PreparedStatement stm = connection.prepareStatement("select id, user, points from player where username = ?");
            stm.setString(1, username);
            ResultSet resultSet = stm.executeQuery();
            resultSet.next();
            return new PlayerFullInfo(resultSet.getInt("id"),
                    resultSet.getString("user"),
                    resultSet.getInt("points"));
        } catch (SQLException ex) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            getPlayerInfo(username);
            System.out.println(ex);
        }
        return null;
    }

    /**
     * ******** End Sign Up And Return PlayerFullInfo model **************
     */
    // -> Update Points
    public boolean updatePoints(int player_id) {
        PreparedStatement p = null;
        try {
            p = connection.prepareStatement("update player set points= (points + 10) where id = ?");
            p.setInt(1, player_id);
            int rs = p.executeUpdate();
            System.out.println("gggggggggggggggggggg: "+rs);
            if (rs == 1) {
                return true;
            }
        } catch (SQLException e) {
//            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
//            updatePoints(player_id);
            System.out.println(e);
        }
        return false;
    }

    /**
     * ************ Start Match Queries or Functionalities ****************
     */
    // -> Get Match using Match id
    public Match getMatch(int match_id) {
        System.out.println("DBConnection > get Match of id: "+match_id);
        PreparedStatement stm = null;
        Match match = new Match();
        try {
            stm = connection.prepareStatement("select * from game where game_id =?");
            stm.setInt(1, match_id);
            ResultSet resultSet = stm.executeQuery();
            
            resultSet.next();
            match.setPlayer1_id(resultSet.getInt("player1_id"));
            match.setPlayer2_id(resultSet.getInt("player2_id"));
            match.setMatch_id(resultSet.getInt("game_id"));
            match.setStatus(resultSet.getString("status"));
            match.setWinner(resultSet.getInt("winner"));
            //match.gamegrid(resultSet.getInt(6)); //gamegrid to be put.
            match.setPlayer1_choice(resultSet.getString("player1_choice"));
            match.setPlayer2_choice(resultSet.getString("player2_choice"));
            match.setMatch_date(resultSet.getTimestamp("game_date"));
            
            System.out.println("The Match is: "+ match);
            
        } catch (SQLException e) {
//            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            //getMatch(match_id);
            System.out.println(e);
        }
        System.out.println("fgfdfg: "+match.getPlayer2_id());
        return match;
    }

    // -> Save Match
    public void saveMatch(Match match, List<Position> postions) {
        //level should be in game, not in player table
        try {
            //check player_id1
            PreparedStatement stms = connection.prepareStatement("insert into game (player1_id,player2_id,status,winner,player1_choice,player2_choice,game_date)"
                    + "values(?,?,?,?,?,?,?);");
            stms.setInt(1, match.getPlayer1_id());
            stms.setInt(2, match.getPlayer2_id());
            stms.setString(3, match.getStatus());
            stms.setInt(4, match.getWinner());
            stms.setString(5, match.getPlayer1_choice());
            stms.setString(6, match.getPlayer2_choice());
            stms.setTimestamp(7, match.getMatch_date());
//            stms.setString(7, "2022-03-14 12-21-55");
            
            stms.execute();
            
            //excute returns boolean, excute query returns object (result set )
            System.out.println("dateeeee: '"+match.getMatch_date()+"'");
                        System.out.println("hello............ heeeeeeeeeeeereeeeeeeeeeeeee");

            int match_id = selectMatchId(match.getMatch_date(), match.getPlayer1_id(), match.getPlayer2_id());
            System.out.println("hhhhhhhhhhhh: "+match_id);
            if (match_id != -1) {
                System.out.println("booom");
                //insert function
                insertPositions(postions, match_id);
            }
        } catch (SQLException ex) {
//            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
//            saveMatch(match, postions);
            System.out.println(ex);
        }
    }

    //to be worked on.
    private void insertPositions(List<Position> positions, int m_id) {
        StringBuilder grid = new StringBuilder("---------");
            try {

                System.out.println("woooooooooooow: "+grid);
                int i = 0;
                for (Position position : positions) {
                    System.out.println("position.getType().charAt(0): "+position.getType().charAt(0));
                                    System.out.println("woooooooooooow: "+grid);

//                    int id = Character.getNumericValue(position.getPosition().charAt(1));
//                    if()
                    grid.setCharAt(i, position.getType().charAt(0));
                    i++;
                }
                System.out.println("Finally: "+grid);
                PreparedStatement stms = connection.prepareStatement("UPDATE game SET game_grid = '"+ grid +"' WHERE game_id = " + m_id + " ");
                stms.executeUpdate();
                System.out.println("rrrrrrrrrrrrrrrrrrrr");
            } catch (SQLException e) {
                System.out.println(e);
            }
//        }
    }

    public void alterMatch(Match match, List<Position> positions) throws SQLException {
        PreparedStatement p = null;
        try {
            p = connection.prepareStatement("update game set game_date=now(), status='finished', winner=? where game_id=?;");
            p.setInt(1, match.getWinner());
            p.setInt(2, match.getMatch_id());
            p.executeUpdate();
        } catch (SQLException ex) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            alterMatch(match, positions);
            System.out.println(ex);
        }

        insertPositions(positions, match.getMatch_id());
    }
    
    public int selectMatchId(Timestamp match_date, int player1_id, int player2_id) {
        int match_id = -1;
        try {
            PreparedStatement stm = connection.prepareStatement("select game_id from game where player1_id=? and player2_id=? and game_date=?");
            stm.setInt(1, player1_id);
            stm.setInt(2, player2_id);
//            String timeStr = match_date.toString();
//            String x = timeStr.substring(0, timeStr.length() - 2);
            stm.setTimestamp(3, match_date);
//            stm.setString(3, "2022-03-14 12-21-55");
            System.out.println("cccccc: uuuuuuuuuuuu hereeeeeeeeeeeeeeeeee");
            ResultSet queryResult = stm.executeQuery();
            queryResult.next();
            match_id = queryResult.getInt("game_id");
            System.out.println("booom: "+match_id);
        } catch (SQLException ex) {
//            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
//            selectMatchId(match_date, player1_id, player2_id);
            System.out.println(ex);
            ex.printStackTrace();
        }
        return match_id;
    }

    // -> Match History
    public List<MatchTable> getMatchHistory(int u_id) {
        List<MatchTable> matches = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("select g.game_date, p.user, pp.user, g.winner, g.status, game_id, g.player1_id, g.player2_id\n"
                    + "from game g, player p, player pp\n"
                    + "where (g.player1_id=p.id and g.player2_id=p.id) or (g.player1_id=pp.id and g.player2_id=p.id) and (g.player1_id=? or g.player2_id=?);");
            pst.setInt(1, u_id);
            pst.setInt(2, u_id);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                return null;
            }
            do {
                String wName = null;
                if (ClientHandler.getPlayerFullInfo(rs.getInt("winner")) != null) {
                    wName = ClientHandler.getPlayerFullInfo(rs.getInt("winner")).getName();
                }
                MatchTable match = new MatchTable();
                match.setMatch_date(rs.getTimestamp(1).toLocalDateTime().toString().split("T")[0]);
                match.setPlayer1_Name(rs.getString(2));
                match.setPlayer2_Name(rs.getString(3));
                match.setWinner(wName);
                match.setStatus(rs.getString(5));
                match.setMatch_id(rs.getInt(6));
                match.setPlayer1_id(rs.getInt(7));
                match.setPlayer2_id(rs.getInt(8));
                matches.add(match);
            } while (rs.next());
        } catch (SQLException ex) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            getMatchHistory(u_id);
            System.out.println(ex);
        }
        return matches;
    }

    // -> Match Table
    public MatchTable getMatchTable(int match_id) {
        try {
            PreparedStatement pst = connection.prepareStatement("select g.game_date, p.user, pp.user, g.winner, g.status, game_id, g.player1_id, g.player2_id\n"
                    + "from game g, player p, player pp\n"
                    + "where (g.player1_id=p.id and g.player2_id=p.id) or (g.player1_id=pp.id and g.player2_id=p.id) and (g.id = ?);");
            pst.setInt(1, match_id);
            ResultSet rs = pst.executeQuery();
            if (!rs.next()) {
                return null;
            }
            String winnerName = null;
            if (ClientHandler.getPlayerFullInfo(rs.getInt("winner")) != null) {
                winnerName = ClientHandler.getPlayerFullInfo(rs.getInt("winner")).getName();
            }
            MatchTable match = new MatchTable();
            match.setMatch_date(rs.getTimestamp(1).toLocalDateTime().toString().split("T")[0]);
            match.setPlayer1_Name(rs.getString(2));
            match.setPlayer2_Name(rs.getString(3));
            match.setWinner(winnerName);
            match.setStatus(rs.getString(5));
            match.setMatch_id(rs.getInt(6));
            match.setPlayer1_id(rs.getInt(7));
            match.setPlayer2_id(rs.getInt(8));
            return match;
        } catch (SQLException ex) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            getMatchTable(match_id);
            System.out.println(ex);
        }
        return null;
    }

    /**
     * ************ End Match Queries or Functionalities ****************
     */
    /**
     * Player Functionalities
     */
    public Map<Integer, PlayerFullInfo> getAllPlayers(boolean eksde) {
        Map<Integer, PlayerFullInfo> players = new HashMap<>();
        try {
            PreparedStatement stm = connection.prepareStatement("select id,username,points from player");
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                int player_id = resultSet.getInt("id");
                players.put(player_id, new PlayerFullInfo(player_id, resultSet.getString("username"), resultSet.getInt("points")));
            }
        } catch (SQLException ex) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            getAllPlayers(eksde);
            System.out.println(ex);
        }
        return players;
    }

    public static Map<Integer, PlayerFullInfo> getAllPlayers() {
        System.out.println("in db");
        Map<Integer, PlayerFullInfo> players = new HashMap<>();
        if (connection != null) {
            try {
                PreparedStatement stm = connection.prepareStatement("select id, user, status, points from player ");
                ResultSet resultSet = stm.executeQuery();
                while (resultSet.next()) {
                    int player_id = resultSet.getInt("id");
                    players.put(player_id,
                            new PlayerFullInfo(
                                    player_id,
                                    resultSet.getString("user"),
                                    resultSet.getInt("points")
                            ));
                }
                System.out.println("got db");
            } catch (SQLException e) {
                try { Thread.sleep(500); } catch (InterruptedException ignored) { }
                getAllPlayers();
                e.printStackTrace();
            }
        } else {
            System.out.println("Can't Enter The Database, Cause of Offline State");
        }
        return players;
    }
    
    public List<Position> getPositions(Match match) {
        List<Position> positions = new ArrayList<>();
        try {
            PreparedStatement pst = connection.prepareStatement("select * from game where game_id = ?");
            pst.setInt(1, match.getMatch_id());
            ResultSet rsmatch = pst.executeQuery();
            if (rsmatch.next()) {
                
                int match_id = rsmatch.getInt("game_id"),
                p1 = rsmatch.getInt("player1_id"), 
                p2 = rsmatch.getInt("player2_id"), 
                winnerId = rsmatch.getInt("winner");

                String status =  rsmatch.getString("status"), 
                grid = rsmatch.getString("game_grid"), 
                date = rsmatch.getString("game_date") ;

                String p1Choice = rsmatch.getString("player1_choice"), 
                p2Choice = rsmatch.getString("player2_choice");

                for(int i = 0; i<grid.length(); i++){
                    int player_id;
                    switch(grid.charAt(i)){
                        case 'X':
                        case 'x':
                            player_id = p1Choice.equalsIgnoreCase("X")? p1 : p2 ;
                            break;
                        case 'O':
                        case 'o':
                            player_id = p1Choice.equalsIgnoreCase("O")? p1 : p2 ;
                            break;
                        default:
                            player_id = -1;
                    }
                    positions.add(new Position(match_id, player_id, "b"+i, grid.charAt(i)+""));
                    
                    System.out.println(grid.charAt(i));
                }
//                positions.add(new Position(rsmatch.getInt("m_id"),
//                        rsmatch.getInt("player_id"),
//                        rsmatch.getString("position")));
            };
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return positions;
    }
}
