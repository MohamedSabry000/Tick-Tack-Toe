package tick.tack.toe.client.controllers;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.text.Position;
import tick.tack.toe.client.TickTackToeClient;
import tick.tack.toe.client.gameAI.AIGameEngine;
import tick.tack.toe.client.models.*;


public class GameVsComputerViewController implements Initializable{
    
    private Map<String, Button> buttons;
    private static final AIGameEngine aiGameEngine = new AIGameEngine();
    private Image imgX, imgO;
    private boolean myTurn;
    private boolean isEasy, viewMod;
    private Match match;
    private List<Position> positions;
    @FXML
    private Button b1, b2, b3, b4, b5, b6, b7, b8, b9;
    
    @FXML
    private Button ResetButton;
    @FXML
    private Button ExitButton;
    @FXML
    private Label lblXPlayer, lblXPlayer1;
    
    @FXML protected void onActionCloseBtn (ActionEvent event){
        Platform.exit();
    }
    @FXML protected void onActionMinBtn (ActionEvent event){
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }    
    @FXML protected void onActionBtn1() {
        placeMove(b1,"b1");
        System.out.println("pressed BS1");
       
    }
    @FXML protected void onActionBtn2() {
        placeMove(b2,"b2");
        System.out.println("pressed BS2");
    }
    @FXML protected void onActionBtn3() {
        placeMove(b3,"b3");
        System.out.println("pressed BS3");
    }
    @FXML protected void onActionBtn4() {
        placeMove(b4,"b4");
        System.out.println("pressed BS4");
    }
    @FXML protected void onActionBtn5() {
        placeMove(b5,"b5");
        System.out.println("pressed BS5");
    }
    @FXML protected void onActionBtn6() {
        placeMove(b6,"b6");
        System.out.println("pressed BS6");
    }
    @FXML protected void onActionBtn7() {
        placeMove(b7,"b7");
        System.out.println("pressed BS7");
    }
    @FXML protected void onActionBtn8() {
        placeMove(b8,"b8");
        System.out.println("pressed BS8");
    }
    @FXML protected void onActionBtn9() {
        placeMove(b9,"b9");
        System.out.println("pressed BS9");
    }
    @FXML protected void onActionReset() {

        System.out.println("pressed BSReset");
           for (Button b : buttons.values()) {
            b.setText("");
            b.setGraphic(new ImageView());
        }
        myTurn = true;
    }
    @FXML protected void onActionExit() {
        System.out.println("pressed BSExit");
        onActionReset();
        TickTackToeClient.openHomeView();
    }
    /* start the logic */
    private void placeMove(Button b, String btnId) {
        if (myTurn && b.getText().equals("")) {
            buttons.get(btnId).setText(String.valueOf(Match.CHOICE_X));
            System.out.println(buttons.values());
            System.out.println(buttons.size());
            buttons.get(btnId).setGraphic(new ImageView(imgX));
            System.out.println(buttons.get(btnId));
            aiGameEngine.setBoard(buttons);
            if (isGameNotEnded()) {
                //try {Thread.sleep(2000);} catch(InterruptedException ignored){}
                aiTurn();
            }
        }
        System.out.println("tick.tack.toe.client.controllers.GameVsComputerViewController.placeMove()");
    }
     private void initButtons() {
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
        private boolean isGameNotEnded() {
        boolean end = false;
        if (aiGameEngine.checkWinner(String.valueOf(Match.CHOICE_O), buttons)) {
            end = true;
            //to be added with views.
            TickTackToeClient.showAlert("Loser", "loser");
            disableScene();
        } else if (aiGameEngine.checkWinner(String.valueOf(Match.CHOICE_X), buttons)) {
            end = true;
            TickTackToeClient.showAlert("Winner", "winner");
            disableScene();
        } else if (aiGameEngine.getAvailableCells().isEmpty()) {
            end = true;
            TickTackToeClient.showAlert("Game Over", "gameOver");
            disableScene();
        }
        return !end;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButtons();
        InputStream inputX = getClass().getResourceAsStream("imgs/x1.png");
        InputStream inputO = getClass().getResourceAsStream("imgs/o1.png");
        imgX = new Image(inputX,55,55,true,true);
        imgO = new Image(inputO, 55,55,true,true);   
        startGame(true);
    }
        private void aiTurn() {
        myTurn = false;
        if (isEasy) {
            aiGameEngine.easy();
        } else {
            aiGameEngine.minMax(0, Match.CHOICE_O);
        }
        buttons.get(aiGameEngine.computerMove).setText(String.valueOf(Match.CHOICE_O));
        buttons.get(aiGameEngine.computerMove).setGraphic(new ImageView(imgO));
        if (isGameNotEnded()) {
            myTurn = true;
        }
    }
    public void startGame(boolean easy) {
        myTurn = true;
        viewMod = false;
        isEasy = easy;
        //lblXPlayer.setText("X");
        //lblXPlayer1.setText("O");
        //TicTacToeClient.sendUpdateInGameStatus(true);
    }
    private void disableScene() {
        //ResetButton.setDisable(true);
        //ExitButton.setDisable(true);
        myTurn = false;
        viewMod = true;
        //imgComputer.setImage(new Image(new File("images/player.png").toURI().toString()));
    }
  //function to retrive data to resume match.
//    private void fillGrid() {
//        String txtChoice;
//        for (Postion position : positions) {
//            Image imgChoice = imgO;
//            if (position.getPlayer_id() == match.getPlayer1_id())
//                txtChoice = match.getPlayer1_choice();
//            else
//                txtChoice = match.getPlayer2_choice();
//
//            if (txtChoice.equals(String.valueOf(Match.CHOICE_X)))
//                imgChoice = imgX;
//
//            buttons.get(position.getPosition()).setText(txtChoice);
//            buttons.get(position.getPosition()).setGraphic(new ImageView(imgChoice));
//        }
//    }
        //set data to resume match.
//        private void setData() {
//        String playerX;
//        String playerO;
//        if (match.getPlayer1_choice().equals(String.valueOf(Match.CHOICE_X))) {
//            playerX = TickTackToeClient.homeController.getPlayerFullInfo(match.getPlayer1_id()).getName();
//            playerO = TickTackToeClient.homeController.getPlayerFullInfo(match.getPlayer2_id()).getName();
//        } else {
//            playerX = TickTackToeClient.homeController.getPlayerFullInfo(match.getPlayer2_id()).getName();
//            playerO = TickTackToeClient.homeController.getPlayerFullInfo(match.getPlayer1_id()).getName();
//        }
//        lblXPlayer.setText(playerX);
//        lblXPlayer1.setText(playerO);
//    }
}