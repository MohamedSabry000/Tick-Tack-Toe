/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.controllers.db;

import java.net.*;
import java.io.*;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import tick.tack.toe.server.controllers.client.ClientHandler;

import tick.tack.toe.server.models.*;

/**
 *
 * @author wwwmo
 */
public class DBConnection {

    private final String dbName = "u635309332_Tick_Tack_Toe";
    private final String dbHost = "109.106.246.1";
    private final String dbPort = "3306";
    private final String dbUser = "u635309332_itiTeam";
    private final String dbPass = "Itians:0)";
    private static Connection connection = null;

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
     * ******** Start Sign In **************
     */
    public int authenticate(Credentials credentials) {
        try {
            System.out.println("Auth satr 1");
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("select id from player where user='"+ credentials.getUsername() +"' and password='" + credentials.getPassword() + "' and status = 'offline' LIMIT 1;");

            System.out.println(rs);
            if (rs.next()) {
                return rs.getInt("id");
            } else {
                 return rs.getInt("id");
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
        if (validateUser(user.getUsername())) {
            return null;
        } else {
            try {
                PreparedStatement stm = connection.prepareStatement("insert into player (username, user, password) values (?,?,?);");
                stm.setString(1, user.getUsername());
                stm.setString(2, user.getName());
                stm.setString(3, user.getPassword());
                System.out.println("hello DBConnection Class -> SignUp Method: "+stm.toString());
                stm.execute();
                System.out.println("Good Job");
                return getPlayerInfo(user.getUsername());
            } catch (SQLException ex) {
                try { Thread.sleep(500); } catch (InterruptedException ignored) { }
                signUp(user);       
                System.out.println(ex);
            }
        }
        return null;
    }

    // -> Validate User Existance
    public boolean validateUser(String username) {
        try {
            PreparedStatement stm = connection.prepareStatement("select * from player where username=?");
            stm.setString(1, username);
            ResultSet result = stm.executeQuery();
            return result.next();
        } catch (SQLException ex) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            validateUser(username); 
            System.out.println(ex);
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
            if (rs == 1) {
                return true;
            }
        } catch (SQLException e) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            updatePoints(player_id);
            System.out.println(e);
        }
        return false;
    }

    /**
     * ************ Start Match Queries or Functionalities ****************
     */
    // -> Get Match using Match id
    public Match getMatch(int match_id) {
        PreparedStatement stm = null;
        Match match = new Match();
        try {
            stm = connection.prepareStatement("select * from game where game_id =?");
            stm.setInt(1, match_id);
            ResultSet resultSet = stm.executeQuery();
            
            resultSet.next();
            match.setPlayer1_id(resultSet.getInt(1));
            match.setPlayer2_id(resultSet.getInt(2));
            match.setMatch_id(resultSet.getInt(3));
            match.setStatus(resultSet.getString(4));
            match.setWinner(resultSet.getInt(5));
            //match.gamegrid(resultSet.getInt(6)); //gamegrid to be put.
            match.setPlayer1_choice(resultSet.getString(7));
            match.setPlayer2_choice(resultSet.getString(8));
            match.setMatch_date(resultSet.getTimestamp(9));
            
        } catch (SQLException e) {
//            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            //getMatch(match_id);
            System.out.println(e);
        }
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
            //excute returns boolean, excute query returns object (result set )
            boolean isInserted = stms.execute();
            if (isInserted) {
                System.out.println("Inserted successfully into the database.");
            }
            int match_id = selectMatchId(match.getMatch_date(), match.getPlayer1_id(), match.getPlayer2_id());
            if (match_id != -1) {
                //insert function
                insertPositions(postions, match_id);
            }
        } catch (SQLException ex) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            saveMatch(match, postions);
            System.out.println(ex);
        }
    }

    //to be worked on.
    private void insertPositions(List<Position> positions, int m_id) {
        StringBuilder grid = new StringBuilder("");
        int i = 0;
        for (Position position : positions) {
            try {
                while (i < 9) {
                    grid.append(position.getPosition().charAt(i));
                    i++;
                }
                Statement stmt = connection.createStatement();
                String queryString = new String("INSERT INTO game (game_grid) values('" + grid + "') where game_id = '" + m_id + "' ");
                ResultSet result = stmt.executeQuery(queryString);
            } catch (SQLException e) {
                try { Thread.sleep(500); } catch (InterruptedException ignored) { }
                insertPositions(positions, m_id);
                System.out.println(e);
            }
        }
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
            PreparedStatement stm = connection.prepareStatement("select game_id from game"
                    + "where player1_id=? and player2_id1=? and game_date=?");
            stm.setInt(1, player1_id);
            stm.setInt(2, player2_id);
            stm.setTimestamp(3, match_date);
            ResultSet queryResult = stm.executeQuery();
            if (queryResult.next()) {
                match_id = queryResult.getInt("game_id");
            }
        } catch (SQLException ex) {
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
            selectMatchId(match_date, player1_id, player2_id);
            System.out.println(ex);
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
            PreparedStatement pst = connection.prepareStatement("select * from game where m_id = ?");
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
                    positions.add(new Position(match_id, player_id, grid.charAt(i)+""));
                    
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
