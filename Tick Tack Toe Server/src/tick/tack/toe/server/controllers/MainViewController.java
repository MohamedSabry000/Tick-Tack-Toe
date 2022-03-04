/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import tick.tack.toe.server.controllers.client.*;

/**
 *
 * @author wwwmo
 */
public class MainViewController {
    private static ClientListener clientListener;
        
    @FXML protected void onActionStartServer(ActionEvent event) {
        clientListener = new ClientListener();
        clientListener.setDaemon(true);
        clientListener.start();
        System.out.println("pressed Start");
    }
    
    @FXML protected void onActionStopServer(ActionEvent event) {
        try{
            clientListener.interrupt();
            clientListener = null;        
            System.out.println("stop");

        }catch (NullPointerException e){
            System.out.println("Server is Allready Down!");
        }  
    }
}
