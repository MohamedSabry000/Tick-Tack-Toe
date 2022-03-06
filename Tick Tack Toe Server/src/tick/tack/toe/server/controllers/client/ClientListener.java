/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.controllers.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author wwwmo
 */
public class ClientListener extends Thread {
    private ServerSocket serverSocket;

    @Override
    public void run() {
        ClientHandler.initPlayerList();
        try {
            serverSocket = new ServerSocket(5050);
            while (true) {
                System.out.println("Waiting for a new client to connect... ");
                Socket socket = serverSocket.accept();
                System.out.println("Connected Successfully");
                new ClientHandler(socket);
            }
        } catch (IOException e) {
            System.out.println("Server Stopped");
        }
    }
    
    @Override
    public void interrupt() throws NullPointerException{
        super.interrupt();
        try {
            serverSocket.close();
            ClientHandler.stopAll();
        } catch (IOException e) {
            System.out.println("what");
        } 
    }
}
