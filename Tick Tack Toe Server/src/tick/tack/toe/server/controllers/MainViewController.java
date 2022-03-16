/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.controllers;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import javafx.application.Platform;
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
import javafx.stage.Stage;


import tick.tack.toe.server.controllers.client.*;
import tick.tack.toe.server.models.PlayerFullInfo;


/**
 *
 * @author wwwmo
 */
public class MainViewController implements Initializable {
    private static ClientListener clientListener;
   
    public  TableView<PlayerFullInfo> tPlayers;
    public  TableColumn<PlayerFullInfo, String> cPlayerName;
    public  TableColumn<PlayerFullInfo, String> cStatus;
    public  TableColumn<PlayerFullInfo, Boolean> cIsInGame;
    public  TableColumn<PlayerFullInfo, Integer> cScore;
    
    public static ObservableList _tableView = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        cPlayerName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        cStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        cIsInGame.setCellValueFactory(new PropertyValueFactory<>("InGame"));
        cScore.setCellValueFactory(new PropertyValueFactory<>("Points"));
        cScore.setComparator(cScore.getComparator().reversed());
        tPlayers.setItems(_tableView);

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
    @FXML protected void onActionCloseBtn (ActionEvent event){
        Platform.exit();
    }
    @FXML protected void onActionMinBtn (ActionEvent event){
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    public static void initClientListener(){
        clientListener = new ClientListener();
        clientListener.setDaemon(true);
        clientListener.start();
    }
    
    public static void fillPlayersTable(Collection<PlayerFullInfo> playersFullInfo) {
        _tableView.clear();
        for (PlayerFullInfo p : playersFullInfo) {
            _tableView.add(p);
            System.out.println(p.getName());
        }
    }    
}
