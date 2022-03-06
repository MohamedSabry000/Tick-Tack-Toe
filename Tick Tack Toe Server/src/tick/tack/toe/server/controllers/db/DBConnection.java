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
 
    public DBConnection(){
        connect();
    }
    
    private void connect(){
        try {
                Class.forName("com.mysql.jdbc.Driver");   
		connection = DriverManager.getConnection("jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName, dbUser, dbPass);   
		System.out.println("connected To DB...");	
        }catch(Exception e){ System.out.println(e);System.out.println("You are Offline!");}   
    }
    
    public static Map<Integer, PlayerFullInfo> getAllPlayers() {
        System.out.println("in db");
        Map<Integer, PlayerFullInfo> players = new HashMap<>();
        if(connection != null){
            try {
                PreparedStatement stm = connection.prepareStatement("select id, user, status, points from player ");
                ResultSet resultSet = stm.executeQuery();
                while (resultSet.next()) {
                    int player_id = resultSet.getInt("id");
                    players.put(player_id,
                            new PlayerFullInfo(player_id,
                                    resultSet.getString("user"),
                                    resultSet.getInt("points")
                            ));
                }
                System.out.println("got db");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Can't Enter The Database, Cause of Offline State");
        }
        
        return players;
    }
}