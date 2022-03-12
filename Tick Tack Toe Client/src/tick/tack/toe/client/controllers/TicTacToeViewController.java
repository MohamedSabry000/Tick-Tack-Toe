
package tick.tack.toe.client.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class TicTacToeViewController {
    
    
    @FXML protected void onActionCloseBtn (ActionEvent event){
        Platform.exit();
    }
    @FXML protected void onActionMinBtn (ActionEvent event){
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

     @FXML protected void onActionBS1(ActionEvent event) {

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
     @FXML protected void onActionAskToPause(ActionEvent event) {

        System.out.println("pressed BS1");
    }
      @FXML protected void onActionFinish(ActionEvent event) {

        System.out.println("pressed BS1");
    }
       @FXML protected void onActionSend(ActionEvent event) {

        System.out.println("pressed BS1");
    }
    
}
