/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.controllers.db;

import java.net.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;
import tick.tack.toe.server.models.*;

/**
 *
 * @author wwwmo
 */
import tick.tack.toe.server.controllers.client.ClientHandler;
public class DBConnection {
    private final String dbName = "u635309332_Tick_Tack_Toe";
    private final String dbHost = "sql480.main-hosting.eu";
    private final String dbPort = "3306";
    private final String dbUser = "u635309332_itiTeam";
    private final String dbPass = "Itians:0)";
    private Connection connection;
 
    public DBConnection()
    {
        connect();
    }
    private void connect()
    {
        try {
                Class.forName("com.mysql.jdbc.Driver");   
		connection = DriverManager.getConnection("jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName, dbUser, dbPass);   
		System.out.println("connected To DB...");	
        }
        catch(ClassNotFoundException | SQLException e){ System.out.println(e);}   
    }
    //incase not handled in clientHandler
    private void closeDataBaseConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public PlayerFullInfo signUp(User user) throws SQLException
    {
        if(validateUser(user.getUsername()))
            return null;
        else
        {
            try 
            {
            PreparedStatement stm = connection.prepareStatement("insert into player (username, user,password) values (?,?,?);");
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getName());
            stm.setString(3, user.getPassword());
            ResultSet result = stm.executeQuery();
            return getPlayerInfo(user.getName());
            }
            catch(SQLException ex)
            {
                System.out.println(ex);
            }
        }
        return null;
    }
    public boolean validateUser(String user_name) throws SQLException
    {
       try
        {
            PreparedStatement stm = connection.prepareStatement("select * from player where username=?");
            stm.setString(1,user_name);
            ResultSet result = stm.executeQuery();
            return result.next();
        }
        catch(SQLException ex)
        {
            System.out.println(ex);
        } 
       return false;
    }
      public boolean updatePoints(int player_id) throws SQLException
      {
        PreparedStatement p = null;
        try {
            p = connection.prepareStatement("update player set points= (points + 10) where id = ?");
            p.setInt(1, player_id);
            int rs = p.executeUpdate();
            if (rs == 1)
                return true;
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

     private PlayerFullInfo getPlayerInfo(String username) throws SQLException
     {
        try 
         {
        PreparedStatement stm = connection.prepareStatement("select id, user, points from player where username = ?");
        stm.setString(1, username);
        ResultSet resultSet = stm.executeQuery();
        resultSet.next();
        return new PlayerFullInfo(resultSet.getInt("id"),
                resultSet.getString("user"),
                resultSet.getInt("points"));
        }
         catch(SQLException ex)
        {
             System.out.println(ex);
        }
         return null;
      }
    
     public Match getMatch(int match_id) throws SQLException
     {
        PreparedStatement stm = null;
        Match match = new Match();
        try {
            stm = connection.prepareStatement("select * from game where game_id =?");
            stm.setInt(1, match_id);
            ResultSet resultSet = stm.executeQuery();
            if (resultSet.next()) {
                match.setPlayer1_id(resultSet.getInt(1));
                match.setPlayer2_id(resultSet.getInt(2));
                match.setMatch_id(resultSet.getInt(3));
                match.setStatus(resultSet.getString(4));
                match.setWinner(resultSet.getInt(5));
                //match.gamegrid(resultSet.getInt(6)); //gamegrid to be put.
                match.setPlayer1_choice(resultSet.getString(7));
                match.setPlayer2_choice(resultSet.getString(8));
                match.setMatch_date(resultSet.getTimestamp(9));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return match;
    }
    public void saveMatch(Match match , List<Postion> postions) throws SQLException
    {
        //level should be in game, not in player table
      try
      {
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
      if(isInserted)
            System.out.println("Inserted successfully into the database.");
      int match_id = selectMatchId(match.getMatch_date(), match.getPlayer1_id(), match.getPlayer2_id());
      if (match_id != -1)
      {
          //insert function
          insertPositions(postions, match_id);
      }
      }
      catch(SQLException ex)
      {
          System.out.println(ex);
      }
    }

    public int selectMatchId(Timestamp match_date,int player1_id, int player2_id) throws SQLException
    {
        int match_id = -1;
        try
        {
            PreparedStatement stm = connection.prepareStatement("select game_id from game"
                    + "where player1_id=? and player2_id1=? and game_date=?");
            stm.setInt(1, player1_id);
            stm.setInt(2, player2_id);
            stm.setTimestamp(3, match_date);
            ResultSet queryResult = stm.executeQuery();
            if(queryResult.next())
            {
                match_id = queryResult.getInt("game_id");
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex);
        }
        return match_id;
    }
    //to be worked on.
    private void insertPositions(List<Postion> positions, int m_id) throws SQLException
    {
       StringBuilder grid = new StringBuilder("");
       int i = 0 ; 
        for (Postion position : positions) {
            try {
                while(i<9)
                {
                    grid.setCharAt(i, position.getPosition());
                    i++;
                }
             Statement stmt = connection.createStatement();
             String queryString = new String("INSERT INTO game (game_grid) values('"+grid+"') where game_id = '"+m_id+"' ");
             ResultSet result = stmt.executeQuery(queryString);
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
    }
    public int authenticate(Credentials credentials) throws SQLException 
    {
         try{
        Statement stmt = connection.createStatement();
        String queryString = new String("select id from player where username='" + credentials.getUsername()+ "' and password='" + credentials.getPassword() + "';");
        ResultSet rs = stmt.executeQuery(queryString);
        if (rs.next())
           { return rs.getInt("id");}
           }
            catch(SQLException  e)
            {
                System.out.println(e);
            }
        return -1;
    }
      
    public Map<Integer,PlayerFullInfo> getAllPlayers(boolean eksde) throws SQLException
    {
        Map<Integer,PlayerFullInfo> players = new HashMap<>();
        try
        {
            PreparedStatement stm = connection.prepareStatement("select id,username,points from player");
            ResultSet resultSet = stm.executeQuery();
            while(resultSet.next())
            {
                int player_id = resultSet.getInt("id");
                players.put(player_id,new PlayerFullInfo(player_id,resultSet.getString("username"),resultSet.getInt("points")));
            }
        }
        catch(SQLException ex)
        {
            System.out.println(ex);
        }
        return players;
    }
    
      public List<PlayerFullInfo> getAllPlayers() throws SQLException
      {
        List<PlayerFullInfo> players = new ArrayList<>();
        try {
            PreparedStatement stm = connection.prepareStatement("select id,username,points from player");
            ResultSet resultSet = stm.executeQuery();
            int index = 0;
            while (resultSet.next()) 
            {
                players.add(new PlayerFullInfo(
                        index,
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getInt("points")
                ));
                index++;
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return players;
    }
      
       public List<MatchTable> getMatchHistory(int u_id) throws SQLException 
       {
        List<MatchTable> matches = new ArrayList<>();
        PreparedStatement pst = connection.prepareStatement("select g.game_date, p.user, pp.user, g.winner, g.status, game_id, g.player1_id, g.player2_id\n" +
                "from game g, player p, player pp\n" +
                "where (g.player1_id=p.id and g.player2_id=p.id) or (g.player1_id=pp.id and g.player2_id=p.id) and (g.player1_id=? or g.player2_id=?);");
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
        return matches;
    }
        public MatchTable getMatchTable(int match_id) 
        {
        try {
            PreparedStatement pst = connection.prepareStatement("select g.game_date, p.user, pp.user, g.winner, g.status, game_id, g.player1_id, g.player2_id\n" +
                "from game g, player p, player pp\n" +
                "where (g.player1_id=p.id and g.player2_id=p.id) or (g.player1_id=pp.id and g.player2_id=p.id) and (g.id = ?);");           
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
            System.out.println(ex);
        }
        return null;
    }

        public void alterMatch(Match match,List<Postion> positions) throws SQLException
        {
        PreparedStatement p = null;
        try {
            p = connection.prepareStatement("update game set game_date=now(), status='finished', winner=? where game_id=?;");
            p.setInt(1, match.getWinner());
            p.setInt(2,match.getMatch_id());
            p.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        //deletePositions(match.getM_id());
        insertPositions(positions, match.getMatch_id());
    }
}