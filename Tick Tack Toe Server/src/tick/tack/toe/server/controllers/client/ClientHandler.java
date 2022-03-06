/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.controllers.client;

import tick.tack.toe.server.controllers.db.*;
//import tick.tack.toe.server.models.*;


import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import tick.tack.toe.server.models.PlayerFullInfo;
import tick.tack.toe.server.controllers.MainViewController;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import tick.tack.toe.server.controllers.MainViewController;


/**
 *
 * @author wwwmo
 */
public class ClientHandler extends Thread {
    private static final DBConnection dbConnection = new DBConnection();
    private Socket mySocket;
    
    private BufferedReader dataInputStream;
    private PrintStream printStream;
    private static Map<Long, ClientHandler> clients = new HashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static Map<Integer, PlayerFullInfo> playersFullInfo;
    private Map<String, IAction> actions;
    private ClientHandler competitor;
    private PlayerFullInfo myFullInfoPlayer;


    
    public ClientHandler(Socket socket) {
//        initActions();
        try {
            dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            clients.put(this.getId(), this);
            System.out.println("No. Clients: " + clients.size());
            mySocket = socket;
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initPlayerList() {
        playersFullInfo = new HashMap<>();
        playersFullInfo = DBConnection.getAllPlayers();
        System.out.println(playersFullInfo.size());
        Platform.runLater(() -> MainViewController.fillPlayersTable(playersFullInfo.values()));
    }
    
    @Override
    public void run() {
        while (true) {
//            try {
//                String jRequest = dataInputStream.readLine();
//                System.out.println(jRequest);
//                JSONObject json = new JSONObject(jRequest);
//                String clientAction = (String) json.get("action");
//                actions.get(clientAction).handleAction(jRequest);
//            } catch (Exception e) {
//                System.out.println("Stopped");
//                dropConnection();
//                System.out.println("No. of Clients: " + clients.size());
//                e.printStackTrace();
//                break;
//            }
        }
    }
    
    public static void stopAll() {
        for (ClientHandler client : clients.values()) {
            client.interrupt();
        }
    }
    
    @Override
    public void interrupt() {
        super.interrupt();
        try {
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    interface IAction {
        void handleAction(String json);
    }
}
