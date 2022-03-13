
package tick.tack.toe.client.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tick.tack.toe.client.TickTackToeClient;
import tick.tack.toe.client.controllers.server.ServerListener;
import tick.tack.toe.client.gameAI.GameEngine;
import tick.tack.toe.client.models.*;
import tick.tack.toe.client.notifications.FinishGameNotification;
import tick.tack.toe.client.notifications.MessageNotification;
import tick.tack.toe.client.notifications.ResumeGameNotification;
import tick.tack.toe.client.requests.AskToPauseRequest;
import tick.tack.toe.client.requests.RejectToPauseRequest;
import tick.tack.toe.client.requests.SaveMatchRequest;
import tick.tack.toe.client.requests.SendMessageRequest;
import tick.tack.toe.client.requests.UpdateBoardRequest;
import tick.tack.toe.client.responses.GetPausedMatchResponse;


public class TicTacToeViewController implements Initializable{
    
    private boolean sent;
    private Match match;
    private List<Position> positions;
    private Map<String, Button> buttons;
    private Image imgChoice;
    private char txtChoice;
    private boolean myTurn = false;
    private final Image imgX = new Image(new File("images/x.png").toURI().toString());
    private final Image imgO = new Image(new File("images/o.png").toURI().toString());
    private final GameEngine gameEngine = new GameEngine();
    
    @FXML
    private Button b1, b2, b3, b4, b5, b6, b7, b8, b9, SendButton, ExirButton, ExirButton1, minBtn, minBtn1, closeBtn;
    @FXML
    private TextField TextField;
    @FXML
    private TextArea ChatArea;
    @FXML
    private Label lblXPlayer, lblOPlayer, lblYourTurn;

    @Override
    public void initialize(URL url, ResourceBundle resources) {
        initButtons();
    }
    
    private void initButtons(){
        buttons = new HashMap<>();
        buttons.put("b1", b1);
        buttons.put("b2", b2);
        buttons.put("b3", b3);
        buttons.put("b4", b4);
        buttons.put("b5", b5);
        buttons.put("b6", b6);
        buttons.put("b7", b7);
        buttons.put("b8", b8);
        buttons.put("b9", b9);
    }
    
    @FXML protected void onActionCloseBtn (ActionEvent event){
        Platform.exit();
    }
    @FXML protected void onActionMinBtn (ActionEvent event){
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML protected void onActionBS1(ActionEvent event) {
        placeMove("b1");
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS2(ActionEvent event) {
        placeMove("b2");
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS3(ActionEvent event) {
        placeMove("b3");
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS4(ActionEvent event) {
        placeMove("b4");
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS5(ActionEvent event) {
        placeMove("b5");
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS6(ActionEvent event) {
        placeMove("b6");
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS7(ActionEvent event) {
        placeMove("b7");
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS8(ActionEvent event) {
        placeMove("b8");
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS9(ActionEvent event) {
        placeMove("b9");
        System.out.println("pressed BS1");
    }
    private void placeMove(String btnId) {
        if (myTurn && buttons.get(btnId).getText().equals("")) {
            myTurn = !myTurn;
            Position position = new Position();
            position.setMatch_id(match.getMatch_id());
            position.setPlayer_id(TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id());
            position.setPosition(btnId);
            UpdateBoardRequest updateBoardReq = new UpdateBoardRequest(position);
            try {
                String jRequest = TickTackToeClient.mapper.writeValueAsString(updateBoardReq);
                ServerListener.sendRequest(jRequest);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
    
    @FXML protected void onActionAskToPause(ActionEvent event) {
        if (!sent) {
            try {
                AskToPauseRequest askToPauseReq = new AskToPauseRequest();
                String jRequest = TickTackToeClient.mapper.writeValueAsString(askToPauseReq);
                ServerListener.sendRequest(jRequest);
                sent = true;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionFinish(ActionEvent event) {
        if (TickTackToeClient.showConfirmation("Finish Match", "Are you sure you want to finish this match? \n" +
                "HINT: you will lose the match.", "Yes", "No")) {
            finishMatch();
        }
        System.out.println("pressed BS1");
    }
    @FXML protected void onActionSend(ActionEvent event) {
        if (IsValidateMessage()) {
            Message message = new Message();
            //setting message and message sender
            message.setMessage(TextField.getText().trim());
            message.setFrom(TickTackToeClient.homeController.getMyPlayerFullInfo().getName());
            //message appearing on chat area
            ChatArea.appendText(TickTackToeClient.homeController.getMyPlayerFullInfo().getName() + " : " + TextField.getText().trim() + "\n");
            //creating request for the server
            SendMessageRequest sendMessageReq = new SendMessageRequest();
            sendMessageReq.setMessage(message);
            try {
                //transforming from object to string json
                String jRequest = TickTackToeClient.mapper.writeValueAsString(sendMessageReq);
                //sending the request
                ServerListener.sendRequest(jRequest);
                //clearing text field
                TextField.clear();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            //sending error message alarm
            TickTackToeClient.showAlert("Message Error", " You can't enter an empty message ", Alert.AlertType.ERROR);
            System.out.println("not a valid msg");
        }
        System.out.println("pressed Send Message");
    }
    
    private boolean IsValidateMessage() {
        //to validate if text in field text is empty
        return !TextField.getText().trim().equals("");
    }
    
    public void handleMessageNotification(MessageNotification messageNotification) {
        //message appearing on chat area
        ChatArea.appendText(messageNotification.getMessage().getFrom()
                + " : " + messageNotification.getMessage().getMessage() + "\n");
    }
    
    public void confirmResume(ResumeGameNotification resumeGameNotification) {
        init();
        TickTackToeClient.openGameView();
        positions = resumeGameNotification.getPositions();
        match = resumeGameNotification.getMatch();
        
        System.out.println("oooooooooooo: "+match.getStatus());
        System.out.println("wwwwwwwwwwwwww: ");

        fillGrid();
        turnAfterResume();
        System.out.println("nnnnnnn: ");
    }
    private void init() {
        sent = false;
        positions = new ArrayList<>();
    }
    private void fillGrid() {
        String txtChoice;
        int i = 1;
        for (Position position : positions) {
            Image imgChoice = null;
            if (position.getPlayer_id() == match.getPlayer1_id())
                txtChoice = match.getPlayer1_choice();
            else if (position.getPlayer_id() == match.getPlayer2_id())
                txtChoice = match.getPlayer2_choice();
            else
                txtChoice = "";
            
            if (txtChoice.equals(String.valueOf(Match.CHOICE_X)))
                imgChoice = imgX;
            else if (txtChoice.equals(String.valueOf(Match.CHOICE_O)))
                imgChoice = imgO;
            
            System.out.println("xxxxxx: "+txtChoice);
            System.out.println("hhhh: "+position.getPosition());
            buttons.get("b"+i).setText(txtChoice);
            if(imgChoice != null )
                buttons.get("b"+i).setGraphic(new ImageView(imgChoice));
            System.out.println("pppppppppppppppp: "+match.getPlayer1_id());
            i++;
        }
    }
    private void turnAfterResume() {
        if (positions.size() % 2 == 0) {
            firstTurn();
        } else {
            myTurn = (match.getPlayer1_id() == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id() && match.getPlayer1_choice().equals(String.valueOf(Match.CHOICE_O)))
                    || (match.getPlayer2_id() == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id() && match.getPlayer2_choice().equals(String.valueOf(Match.CHOICE_O)));

            txtChoice = Match.CHOICE_O;
            imgChoice = imgO;
        }
        setTurnLabel();
    }
    private void firstTurn() {
        myTurn = (match.getPlayer1_id() == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id()&& match.getPlayer1_choice().equals(String.valueOf(Match.CHOICE_X)))
                || (match.getPlayer2_id() == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id()&& match.getPlayer2_choice().equals(String.valueOf(Match.CHOICE_X)));

        txtChoice = Match.CHOICE_X;
        imgChoice = imgX;
        setTurnLabel();
    }
    private void setTurnLabel() {
        if (myTurn)
            lblYourTurn.setText("Your Turn");
        else
            lblYourTurn.setText("Competitor Turn");
    }
    
    public void handleUpdateBoard(Position position) {
        buttons.get(position.getPosition()).setGraphic(new ImageView(imgChoice));
        buttons.get(position.getPosition()).setText(String.valueOf(txtChoice));
        positions.add(position);
        checkMatchResult(position.getPlayer_id());
        turn();

        if (!(position.getPlayer_id() == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id())) {
            myTurn = !myTurn;
        }
        setTurnLabel();
    }
    private void checkMatchResult(int playerId) {
        if (gameEngine.checkWinner(String.valueOf(txtChoice), buttons) && playerId == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id()) {
            match.setWinner(TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id());
            match.setStatus(Match.STATUS_FINISHED);
            saveMatch();
            showMatchResult(playerId);
            backToHome();
        } else if (positions.size() == 9 && playerId == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id()) {
            match.setStatus(Match.STATUS_FINISHED);
            saveMatch();
            showMatchResult(-1);
            backToHome();
        }
    }
    
    public void handlePauseGame() {
        String title = "Game Paused";
        String msg = "Your competitor accept to pause the game.";
        TickTackToeClient.showAlert(title, msg, Alert.AlertType.INFORMATION);
        backToHome();
    }
    
    public void handleFinishGame(FinishGameNotification finishGameNotification) {
        showMatchResult(finishGameNotification.getWinner());
    }
    private void showMatchResult(int winner) {
        String title;
        String img;
        String msg;
        if (winner == -1) {
            title = "Game Over";
            msg = "Game Over";
            img = "gameOver";
        } else if (winner == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id()) {
            title = "You Won";
            msg = "WINNER";
            img = "winner";
        } else {
            title = "Good luck next time";
            msg = title;
            img = "loser";
        }
//        TickTackToeClient.showSystemNotification(title, msg, TrayIcon.MessageType.INFO);
        TickTackToeClient.showAlert(title, msg, Alert.AlertType.INFORMATION);
        backToHome();
    }
    
    
    public void handleAskToPauseResponse() {
        String title = "Rejected Pause Request";
        String msg = "It seems your competitor can not pause the match";
        if (!TickTackToeClient.showConfirmation(title, msg, "Continue Game", "Finish Game")) {
            finishMatch();
        }
    }
    private void finishMatch() {
        match.setStatus(Match.STATUS_FINISHED);
        if (match.getPlayer1_id() == TickTackToeClient.homeController.getMyPlayerFullInfo().getDb_Player_id()) {
            match.setWinner(match.getPlayer2_id());
        } else {
            match.setWinner(match.getPlayer1_id());
        }
        saveMatch();
        backToHome();
    }
    
    public void startMatch(Match match) {
        this.match = match;
        init();
        firstTurn();
        setData();
    }
    private void setData() {
        String playerX;
        String playerO;
        if (match.getPlayer1_choice().equals(String.valueOf(Match.CHOICE_X))) {
            playerX = TickTackToeClient.homeController.getPlayerFullInfo(match.getPlayer1_id()).getName();
            playerO = TickTackToeClient.homeController.getPlayerFullInfo(match.getPlayer2_id()).getName();
        } else {
            playerX = TickTackToeClient.homeController.getPlayerFullInfo(match.getPlayer2_id()).getName();
            playerO = TickTackToeClient.homeController.getPlayerFullInfo(match.getPlayer1_id()).getName();
        }
        lblXPlayer.setText(playerX);
        lblOPlayer.setText(playerO);
    }
    
    public void viewMatchHistory(GetPausedMatchResponse getPausedMatchRes) {
        positions = getPausedMatchRes.getPositions();
        match = getPausedMatchRes.getMatch();
        TickTackToeClient.openGameView();
        fillGrid();
    }
    
    private void saveMatch() {
        SaveMatchRequest saveMatchReq = new SaveMatchRequest(match, positions);
        try {
            String jRequest = TickTackToeClient.mapper.writeValueAsString(saveMatchReq);
            ServerListener.sendRequest(jRequest);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    public void competitorConnectionIssue() {
        String title = "Competitor Connection Issue";
        String msg = "It seems your competitor disconnected from the server.";
        if (TickTackToeClient.showConfirmation(title, msg, "Save Game", "End Game")) {
            match.setStatus(Match.STATUS_PAUSED);
            saveMatch();
        } else {
            TickTackToeClient.sendUpdateInGameStatus(false);
        }
        backToHome();
    }
    
    public void notifyAskToPause() {
        String msg = "Your competitor would like to pause this game now.";
        String title = "Ask To Pause";
        if (TickTackToeClient.showConfirmation(title, msg, "Accept", "Reject")) {
            match.setStatus(Match.STATUS_PAUSED);
            saveMatch();
            backToHome();
        } else {
            RejectToPauseRequest rejectToPauseReq = new RejectToPauseRequest();
            try {
                String jRequest = TickTackToeClient.mapper.writeValueAsString(rejectToPauseReq);
                ServerListener.sendRequest(jRequest);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Most Used Methods
    private void backToHome() {
        match = null;
        positions.clear();
        ChatArea.clear();
        TickTackToeClient.openHomeView();
        reset();
    }
    protected void reset() {
        for (Button b : buttons.values()) {
            b.setText("");
            b.setGraphic(new ImageView());
        }
        myTurn = false;
        lblYourTurn.setText("");
        lblXPlayer.setText("");
        lblOPlayer.setText("");
    }
    private void turn() {
        if (txtChoice == Match.CHOICE_X) {
            txtChoice = Match.CHOICE_O;
            imgChoice = imgO;
        } else {
            txtChoice = Match.CHOICE_X;
            imgChoice = imgX;
        }
    }
}
