/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import tick.tack.toe.client.TickTackToeClient;
import tick.tack.toe.client.models.Match;
import tick.tack.toe.client.models.MatchTable;




public class MatchViewController implements Initializable{

    @FXML
    private TableView<MatchTable> MatchTable;
    @FXML
    private TableColumn<Match, String> dateColumn;
    @FXML
    private TableColumn<Match, String> player1Column;
    @FXML
    private TableColumn<Match, String> player2Column;
    @FXML
    private TableColumn<Match, String> statusColumn;
    @FXML
    private TableColumn<Match, String> winnerColumn;
    @FXML
    private Button resumeButton;
    @FXML
    private Button viewButton;
    @FXML
    private Button backButton;
    
    private MatchTable matchTable;
    private List<MatchTable> matchesList;
    private int selectedIndex;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("match_date"));
//        player1Column.setCellValueFactory(new PropertyValueFactory<>("player1_Name"));
//        player2Column.setCellValueFactory(new PropertyValueFactory<>("player2_Name"));
//        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
//        winnerColumn.setCellValueFactory(new PropertyValueFactory<>("winner"));
    }
    
    @FXML protected void onActionBack(ActionEvent event) {
        TickTackToeClient.openHomeView();
    }
    @FXML protected void onActionResume(ActionEvent event) {
    }
    @FXML protected void onActionView(ActionEvent event) {
//        try {
//            matchTable = MatchTable.getSelectionModel().getSelectedItem();
//            if (matchTable != null) {
//                GetPausedMatchReq getPausedMatchReq = new GetPausedMatchReq(matchTable.getM_id());
//                String jRequest = TickTackToeClient.mapper.writeValueAsString(getPausedMatchReq);
//                ServerListener.sendRequest(jRequest);
//            }
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
    } 
}

