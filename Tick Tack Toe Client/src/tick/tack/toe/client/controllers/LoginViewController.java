
package tick.tack.toe.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;


public class LoginViewController {
     @FXML protected void onActionLogin(ActionEvent event) {

        System.out.println("pressed Login");
    }
     @FXML protected void onActionRegister(ActionEvent event) {

        System.out.println("pressed Register");
    }
     @FXML protected void onActionGuest(ActionEvent event) {

        System.out.println("pressed Guest");
    }
}
