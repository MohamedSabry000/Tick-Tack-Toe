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
import java.util.*;

/**
 *
 * @author wwwmo
 */
public class DBConnection {
    private final String dbName = "u635309332_Tick_Tack_Toe";
    private final String dbHost = "sql480.main-hosting.eu";
    private final String dbPort = "3306";
    private final String dbUser = "u635309332_itiTeam";
    private final String dbPass = "Itians:0)";
    private Connection connection;
 
    public DBConnection(){
        connect();
    }
    
    private void connect(){
        try {
                Class.forName("com.mysql.jdbc.Driver");   
		connection = DriverManager.getConnection("jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName, dbUser, dbPass);   
		System.out.println("connected To DB...");	
        }catch(Exception e){ System.out.println(e);}   
    }
}