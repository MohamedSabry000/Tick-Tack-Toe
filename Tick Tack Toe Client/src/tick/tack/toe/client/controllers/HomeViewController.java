/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.awt.TrayIcon.MessageType;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import tick.tack.toe.client.TickTackToeClient;
import tick.tack.toe.client.controllers.server.ServerListener;
import tick.tack.toe.client.models.Invitation;
import tick.tack.toe.client.models.Player;
import tick.tack.toe.client.models.PlayerFullInfo;
import tick.tack.toe.client.requests.InviteToGameRequest;




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
        invitations = new HashMap<>();
        sent = new HashMap<>();
        
        cPlayerName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        cStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        cIsInGame.setCellValueFactory(new PropertyValueFactory<>("InGame")); 
        //tPlayers.setItems();
        
        cFrom.setCellValueFactory(new PropertyValueFactory<>("Name"));
        cNotif.setCellValueFactory(new PropertyValueFactory<>("Type"));
        
        tInvitation.setRowFactory(tv -> {
            TableRow<Invitation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    if(tInvitation.getSelectionModel().getSelectedItem().getType() == Invitation.GAME_INVITATION)
                        showInvitationConfirmation();
//                    else
//                        respondToResumeReq();
                }
            });
            return row;
        });

    }
    // to confirm user
    private void showInvitationConfirmation() {
        Invitation invitation = tInvitation.getSelectionModel().getSelectedItem();
        if (invitation.getType().equals(Invitation.GAME_INVITATION)) {
//            confirmGameInvitation(invitation);
        }
    }
    @FXML
    protected void onActionLogin(ActionEvent event) {

        TickTackToeClient.openLoginView();
    }
    @FXML protected void onActionMatch(ActionEvent event) {

        System.out.println("pressed Match");
    }
    @FXML protected void onActionVsComputer(ActionEvent event) {

        System.out.println("pressed VsComputer");
        TickTackToeClient.openGameVsComputerView();
    }
    @FXML protected void onActionInvitePlayer(ActionEvent event) {

        System.out.println("pressed Invite Player");
        // get selected object
        PlayerFullInfo playerFullInfo = tPlayers.getSelectionModel().getSelectedItem();
        // check if the selected object is valid and not sent him invite before
        if (isValidSelection(playerFullInfo) && sent.get(playerFullInfo.getDb_Player_id()) == null) {
            // create invite to a game request
            InviteToGameRequest inviteToGameReq = new InviteToGameRequest(new Player(playerFullInfo));
            try {
                // convert the request to string
                String jRequest = TickTackToeClient.mapper.writeValueAsString(inviteToGameReq);
                // send the request
                ServerListener.sendRequest(jRequest);
                // add request to sent
                sent.put(playerFullInfo.getDb_Player_id(), playerFullInfo);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
    private boolean isValidSelection(PlayerFullInfo playerFullInfo) {
        boolean valid = false;
        if (playerFullInfo == null) {
            TickTackToeClient.showAlert("Error", "You have to select a player first", Alert.AlertType.ERROR);
            // if selected player is in game
        } else if (playerFullInfo.isInGame()) {
            TickTackToeClient.showAlert("Error", "You have to select a player which is not in game", Alert.AlertType.ERROR);
            // if selected player is offline
        } else if (playerFullInfo.getStatus().equals(PlayerFullInfo.OFFLINE)) {
            TickTackToeClient.showAlert("Error", "You have to select an online player", Alert.AlertType.ERROR);
        } else {
            valid = true;
        }
        return valid;
    }
    
    /**
     * Start Functionality
     */
    public void fromLogin(PlayerFullInfo myPlayerFullInfo, Map<Integer, PlayerFullInfo> playersFullInfo) {
        this.myPlayerFullInfo = myPlayerFullInfo;
        this.playersFullInfo = playersFullInfo;
        System.out.println(myPlayerFullInfo.getDb_Player_id());
        System.out.println(playersFullInfo.values());
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
        vsComputerButton.setDisable(isOffline);
        showHideLoginBtn(isOffline);
    }
    public void showHideLoginBtn(boolean isOffline){
        btnLogin.setVisible(myPlayerFullInfo == null && !isOffline);
    }
    
    public PlayerFullInfo getMyPlayerFullInfo() {
        return myPlayerFullInfo;
    }
    
    public void updateStatus(PlayerFullInfo playerFullInfo) {
        if (playersFullInfo != null) {
            if (playerFullInfo.getDb_Player_id()== myPlayerFullInfo.getDb_Player_id()) {

                lblScore.setText(String.valueOf(playerFullInfo.getPoints()));
                TickTackToeClient.showAlert("Back Online", "You are now online", Alert.AlertType.INFORMATION);
            } else {
                if (!playerFullInfo.getStatus().equals(playersFullInfo.get(playerFullInfo.getDb_Player_id()).getStatus())) {
                    if(playerFullInfo.getStatus().equals(PlayerFullInfo.ONLINE)){
                        playersFullInfo.get(playerFullInfo.getDb_Player_id()).setStatus(PlayerFullInfo.ONLINE);
                    } else {
                        playersFullInfo.get(playerFullInfo.getDb_Player_id()).setStatus(PlayerFullInfo.OFFLINE);
                    }
                }
                //playersFullInfo.put(playerFullInfo.getDb_Player_id(), playerFullInfo);
                fillPlayersTable();
            }
        }
    }
    
    public PlayerFullInfo getPlayerFullInfo(int id) {
        if (myPlayerFullInfo.getDb_Player_id()== id) {
            return myPlayerFullInfo;
        }
        return playersFullInfo.get(id);
    }

    public void notifyGameInvitation(Player player) {
        // check if received this notification before
        if (invitations.get(player.getDb_Player_id()) == null) {
            Invitation invitation = new Invitation();
            invitation.setType(Invitation.GAME_INVITATION);
            invitation.setPlayer(player);
            invitation.setName(playersFullInfo.get(player.getDb_Player_id()).getName());
            invitations.put(invitation.getPlayer().getDb_Player_id(), invitation);
            fillInvitationsTable();
//            TickTackToeClient.showSystemNotification("Game Invitation",
//                    playersFullInfo.get(player.getDb_Player_id()).getName() + " sent you game invitation.",
//                    MessageType.INFO);
        }
    }
    private void fillInvitationsTable() {
        tInvitation.getItems().clear();
        tInvitation.getItems().setAll(invitations.values());
    }
}

