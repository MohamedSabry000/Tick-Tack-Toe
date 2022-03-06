/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.controllers.server;

import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Map;

import javafx.application.Platform;
import tick.tack.toe.client.TickTackToeClient;

/**
 *
 * @author wwwmo
 */
public class ServerListener extends Thread {

    private static final int PORT = 5050;
    private static final String HOST = "localhost";
    private static PrintStream printStream;
    private Socket socket;
    private BufferedReader bufferedReader;
    private Map<String, IType> types;
    private boolean running, first;

    public ServerListener() {
        running = true;
        first = true;
//        initTypes();
        initConnection();
    }

    private void initConnection() {
        try {
            socket = new Socket(HOST, PORT);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            System.out.println("connected");
//            backFromOffline();
            first = false;
        } catch (Exception ex) {
            System.out.println("Failed to Connect Due to the Server is Down");
            if (first) {
                first = false;
                goOffline();
            }
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) { }
            
            initConnection();
        }
    }
    
    private void goOffline() {
        if (!first) {
            Platform.runLater(() -> {
                TickTackToeClient.showSystemNotification("Disconnected",
                        "Unfortunately disconnected from the server please check your internet connection",
                        TrayIcon.MessageType.INFO);

//                TickTackToeClient.homeController.offline(true);
                if (TickTackToeClient.openedScene != TickTackToeClient.scenes.vsComputerS && TickTackToeClient.openedScene != TickTackToeClient.scenes.homeS) {
                    TickTackToeClient.openHomeView();
                }
            });
        }
    }
    

    @Override
    public void run() {
        while (running) {
//            try {
//                String sMessage = bufferedReader.readLine();
//                System.out.println(sMessage);
//                JSONObject json = new JSONObject(sMessage);
//                String serverType = (String) json.get("type");
//                if (types.get(serverType) != null) {
//                    types.get(serverType).handleAction(sMessage);
//                }
//            } catch (Exception e) {
//                if (running) {
//                    goOffline();
//                    initConnection();
//                }
//            }
        }
    }
    
    @Override
    public void interrupt() throws NullPointerException{
        super.interrupt();
        try {
            System.out.println("h");
            socket.close();
                        System.out.println("1h");

            first=false;
                        System.out.println("2h");

            running=false;
                        System.out.println("3h");

            goOffline();
        } catch (IOException e) {
            System.out.println("what");
        } 
    }

    interface IType {

        void handleAction(String json);
    }
}