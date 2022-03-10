/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.controllers;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import tick.tack.toe.client.models.Invitation;
import tick.tack.toe.client.models.Player;
import tick.tack.toe.client.models.PlayerFullInfo;




public class HomeViewController implements Initializable {

    private Map<Integer, PlayerFullInfo> playersFullInfo;
    private PlayerFullInfo myPlayerFullInfo;
    private Map<Integer, Invitation> invitations;
    private Map<Integer, Player> sent;
    
    @FXML
    private TableView<PlayerFullInfo> tPlayers;
    @FXML
    private TableColumn<PlayerFullInfo, String> cPlayerName;
    @FXML
    private TableColumn<PlayerFullInfo, String> cStatus;
    @FXML
    private TableColumn<PlayerFullInfo, String> cIsInGame;
    @FXML
    private TableView<Invitation> tInvitation;
    @FXML
    private TableColumn<Invitation, String> cFrom;
    @FXML
    private TableColumn<Invitation, String> cNotif;
    
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnMatches;
    @FXML
    private Button btnInvite;
    @FXML
    private Button vsComputerButton;
    @FXML
    private Label lblName;
    @FXML
    private Label lblScore;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
         cPlayerName.setCellValueFactory(new PropertyValueFactory<>("Name"));
         cStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
         cIsInGame.setCellValueFactory(new PropertyValueFactory<>("InGame")); 
         //tPlayers.setItems();
    }
    
    protected void onActionLogin(ActionEvent event) {

        System.out.println("pressed Login");
    }
    @FXML protected void onActionMatch(ActionEvent event) {

        System.out.println("pressed Match");
    }
    @FXML protected void onActionVsComputer(ActionEvent event) {

        System.out.println("pressed VsComputer");
    }
    @FXML protected void onActionInvitePlayer(ActionEvent event) {

        System.out.println("pressed Invite Player");
    }
    
    /**
     * Start Functionality
     */
    public void fromLogin(PlayerFullInfo myPlayerFullInfo, Map<Integer, PlayerFullInfo> playersFullInfo) {
        this.myPlayerFullInfo = myPlayerFullInfo;
        this.playersFullInfo = playersFullInfo;
        playersFullInfo.remove(myPlayerFullInfo.getDb_Player_id());
        fillView();
//        sent.clear();
        offline(false);
    }
    private void fillView() {
        fillPlayersTable();
        lblName.setText(myPlayerFullInfo.getName());
        lblScore.setText(String.valueOf(myPlayerFullInfo.getPoints()));
    }
    private void fillPlayersTable() {
        tPlayers.getItems().clear();
        tPlayers.getItems().setAll(playersFullInfo.values());

        tPlayers.getSortOrder().add(cStatus);
    }
    public void offline(boolean isOffline) {
        tInvitation.setDisable(isOffline);
        tPlayers.setDisable(isOffline);
        btnMatches.setDisable(isOffline);
        btnInvite.setDisable(isOffline);
        showHideLoginBtn(isOffline);
    }
    public void showHideLoginBtn(boolean isOffline){
        btnLogin.setVisible(myPlayerFullInfo == null && !isOffline);
    }
}

