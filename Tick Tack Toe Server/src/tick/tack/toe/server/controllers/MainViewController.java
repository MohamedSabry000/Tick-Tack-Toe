/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.controllers;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.scene.control.cell.PropertyValueFactory;


import tick.tack.toe.server.controllers.client.*;
import tick.tack.toe.server.models.PlayerFullInfo;


/**
 *
 * @author wwwmo
 */
public class MainViewController implements Initializable {
    private static ClientListener clientListener = null;
    @FXML private static TableView<PlayerFullInfo> tPlayers;
    @FXML private static TableColumn<PlayerFullInfo, String> cPlayerName;
    @FXML private static TableColumn<PlayerFullInfo, String> cStatus;
    @FXML private static TableColumn<PlayerFullInfo, Boolean> cIsInGame;
    @FXML private static TableColumn<PlayerFullInfo, Integer> cScore;
    
    public static ObservableList _tableView = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        cPlayerName.setCellValueFactory(new PropertyValueFactory<>("user"));
//        cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
//        cIsInGame.setCellValueFactory(new PropertyValueFactory<>("inGame"));
//        cScore.setCellValueFactory(new PropertyValueFactory<>("points"));
//        cStatus.setComparator(cStatus.getComparator().reversed());
    }
    
    @FXML protected void onActionStartServer(ActionEvent event) {
        if(clientListener == null){
            clientListener = new ClientListener();
            clientListener.setDaemon(true);
            clientListener.start();
            System.out.println("pressed Start");
        } else {
            System.out.println("Sorry, The Server is Allready Active!");
        }
        
    }
    
    @FXML protected void onActionStopServer(ActionEvent event) {
        try{
            if(clientListener != null ){
                clientListener.interrupt();
                clientListener = null;        
                System.out.println("stop");
            } else {
                System.out.println("Sorry, The Server is Allready Down!");
            }  
        }catch (NullPointerException e){
            System.out.println("Server is Allready Down!");
        }  
    }
    
    public static void initClientListener(){
        clientListener = new ClientListener();
        clientListener.setDaemon(true);
        clientListener.start();
    }
    
    public static void fillPlayersTable(Collection<PlayerFullInfo> playersFullInfo) {
        
        for (PlayerFullInfo p : playersFullInfo) {
            _tableView.add(p);
            System.out.println(p.getName());
        }
        
//        System.out.println(playersFullInfo);
//cPlayerName.setText("Good Luck");
//        System.out.println(cPlayerName.getId());
        System.out.println("what");
        cPlayerName.setCellValueFactory( new PropertyValueFactory<>("name") );
                System.out.println("what2");

        cStatus.setCellValueFactory( new PropertyValueFactory<>("status") );
        cIsInGame.setCellValueFactory( new PropertyValueFactory<>("inGame") );
        cScore.setCellValueFactory( new PropertyValueFactory<>("points") );
        
        tPlayers.setItems(_tableView);
//        tPlayers.getItems().clear();
//        tPlayers.getItems().setAll(playersFullInfo);
//        tPlayers.getSortOrder().add(cStatus);
    }    
}
