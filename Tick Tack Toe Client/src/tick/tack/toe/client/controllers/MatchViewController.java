/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tick.tack.toe.client.TickTackToeClient;
import tick.tack.toe.client.controllers.server.ServerListener;
import tick.tack.toe.client.models.Match;
import tick.tack.toe.client.models.MatchTable;
import tick.tack.toe.client.models.Player;
import tick.tack.toe.client.requests.AskToResumeRequest;
import tick.tack.toe.client.requests.GetPausedMatchRequest;




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
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Match_date"));
        player1Column.setCellValueFactory(new PropertyValueFactory<>("Player1_Name"));
        player2Column.setCellValueFactory(new PropertyValueFactory<>("Player2_Name"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("Status"));
        winnerColumn.setCellValueFactory(new PropertyValueFactory<>("Winner"));
    }
    
    @FXML protected void onActionCloseBtn (ActionEvent event){
        Platform.exit();
    }
    @FXML protected void onActionMinBtn (ActionEvent event){
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    
    @FXML protected void onActionBack(ActionEvent event) {
        TickTackToeClient.openHomeView();
    }
    @FXML protected void onActionResume(ActionEvent event) {
        if (MatchTable.getSelectionModel().getSelectedItem() != null) {
            matchTable = MatchTable.getSelectionModel().getSelectedItem();
            selectedIndex = MatchTable.getSelectionModel().getSelectedIndex();
            
            askToResumeReq(matchTable);
        }
    }
    @FXML protected void onActionView(ActionEvent event) {
        try {
            matchTable = MatchTable.getSelectionModel().getSelectedItem();
            if (matchTable != null) {
                GetPausedMatchRequest getPausedMatchReq = new GetPausedMatchRequest(matchTable.getMatch_id());
                String jRequest = TickTackToeClient.mapper.writeValueAsString(getPausedMatchReq);
                ServerListener.sendRequest(jRequest);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void askToResumeReq(MatchTable matchTable) {
        System.out.println(matchTable.getMatch_date()+" "+matchTable.getPlayer1_Name()+" "+matchTable.getPlayer1_id()+" "+matchTable.getPlayer2_id());
        int db_id;
        if (Objects.equals(matchTable.getStatus().toLowerCase(Locale.ROOT), tick.tack.toe.client.models.MatchTable.STATUS_PAUSED)) {
            if (matchTable.getPlayer1_id() == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id()) {
                db_id = matchTable.getPlayer2_id();
            } else {
                db_id = matchTable.getPlayer1_id();
            }
            long s_id = TickTackToeClient.homeController.getPlayerFullInfo(db_id).getServer_id();
            System.out.println("zerooooooooooooo: "+s_id);
            boolean answer = TickTackToeClient.showConfirmation("Resume game", "Send Resume Request?", "Ok", "Cancel");
            System.out.println(answer);
            if (answer) {
                Player player = new Player(db_id, s_id);
                Match match = new Match();
                match.setMatch_id(matchTable.getMatch_id());
                AskToResumeRequest askToResumeReq = new AskToResumeRequest();
                askToResumeReq.setPlayer(player);
                askToResumeReq.setMatch(match);
                try {
                    String jRequest = TickTackToeClient.mapper.writeValueAsString(askToResumeReq);
                    ServerListener.sendRequest(jRequest);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

            }

        }

    }
    
    public void fillMatchesTable(List<MatchTable> matchList) {
        matchesList= matchList;
        MatchTable.getItems().clear();
        MatchTable.getItems().setAll(matchList);
    }
}

