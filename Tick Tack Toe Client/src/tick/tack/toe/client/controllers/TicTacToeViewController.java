
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
import tick.tack.toe.client.notifications.MessageNotification;
import tick.tack.toe.client.notifications.ResumeGameNotification;
import tick.tack.toe.client.requests.AskToPauseRequest;
import tick.tack.toe.client.requests.SendMessageRequest;
import tick.tack.toe.client.requests.UpdateBoardRequest;


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
    private Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
    @FXML
    private TextField TextField;
    @FXML
    private TextArea ChatArea;
    
    @FXML
    private Button SendButton;
    @FXML
    private Label lblYourTurn;
    @FXML
    private Button ExirButton;
    @FXML
    private Button ExirButton1;
    
    @FXML
    private Button minBtn;
    @FXML
    private Button minBtn1;
    @FXML
    private Button closeBtn;

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

        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS3(ActionEvent event) {

        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS4(ActionEvent event) {

        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS5(ActionEvent event) {

        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS6(ActionEvent event) {

        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS7(ActionEvent event) {

        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS8(ActionEvent event) {

        System.out.println("pressed BS1");
    }
    @FXML protected void onActionBS9(ActionEvent event) {

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
        fillGrid();
        turnAfterResume();
    }
    private void init() {
        sent = false;
        positions = new ArrayList<>();
    }
    private void fillGrid() {
        String txtChoice;
        for (Position position : positions) {
            Image imgChoice = imgO;
            if (position.getPlayer_id() == match.getPlayer1_id())
                txtChoice = match.getPlayer1_choice();
            else
                txtChoice = match.getPlayer2_choice();

            if (txtChoice.equals(String.valueOf(Match.CHOICE_X)))
                imgChoice = imgX;

            buttons.get(position.getPosition()).setText(txtChoice);
            buttons.get(position.getPosition()).setGraphic(new ImageView(imgChoice));
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
}
