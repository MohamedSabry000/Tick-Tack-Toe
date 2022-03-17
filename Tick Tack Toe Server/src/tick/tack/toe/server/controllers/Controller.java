///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package tick.tack.toe.server.controllers;
//
////import com.iti.tictactoeserver.models.PlayerFullInfo;
//import javafx.fxml.FXML;
////import com.iti.tictactoeserver.helpers.server.ClientListener;
//import javafx.fxml.Initializable;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//
//import java.net.URL;
//import java.util.Collection;
//import java.util.ResourceBundle;
//import tick.tack.toe.server.controllers.client.ClientListener;
//import tick.tack.toe.server.models.PlayerFullInfo;
///**
// *
// * @author wwwmo
// */
//public class Controller implements Initializable  {
//    
//    private static ClientListener clientListener;
//
//    @FXML
//    private TableView<PlayerFullInfo> tPlayers;
//    @FXML
//    private TableColumn<PlayerFullInfo, String> cPlayerName;
//    @FXML
//    private TableColumn<PlayerFullInfo, String> cStatus;
//    @FXML
//    private TableColumn<PlayerFullInfo, Boolean> cIsInGame;
//    @FXML
//    private TableColumn<PlayerFullInfo, Integer> cScore;
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        cPlayerName.setCellValueFactory(new PropertyValueFactory<>("name"));
//        cStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
//        cIsInGame.setCellValueFactory(new PropertyValueFactory<>("inGame"));
//        cScore.setCellValueFactory(new PropertyValueFactory<>("points"));
//        cScore.setComparator(cScore.getComparator().reversed());
//    }
//
//    @FXML
//    protected void onActionStart() {
//        clientListener = new ClientListener();
//        clientListener.setDaemon(true);
//        clientListener.start();
//    }
//
//    @FXML
//    protected void onActionStop() {
//        clientListener.interrupt();
//        clientListener = null;
//    }
//
//    public void fillPlayersTable(Collection<PlayerFullInfo> playersFullInfo) {
//        tPlayers.getItems().clear();
//        tPlayers.getSortOrder().add(cScore);
//        tPlayers.getItems().setAll(playersFullInfo);
//    }
//
//
//}