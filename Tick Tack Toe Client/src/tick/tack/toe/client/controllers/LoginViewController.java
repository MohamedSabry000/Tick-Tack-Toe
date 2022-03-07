
package tick.tack.toe.client.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.awt.TrayIcon;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import tick.tack.toe.client.TickTackToeClient;
import tick.tack.toe.client.controllers.server.ServerListener;
import tick.tack.toe.client.models.*;
import tick.tack.toe.client.requests.LoginRequest;
import tick.tack.toe.client.responses.LoginResponse;
import tick.tack.toe.client.responses.Response;


public class LoginViewController implements Initializable{

    private Map<Integer, PlayerFullInfo> playersFullInfo;
    private PlayerFullInfo myPlayerFullInfo;
    private Map<Integer, Invitation> invitations;
    private Map<Integer, Player> sent;
    
    @FXML
    private TextField UserNameTxt;
    @FXML
    private PasswordField PasswordTxt;
    @FXML
    private Label invaliduserTxt;
    
    @FXML protected void onActionLogin(ActionEvent event) {
        System.out.println("pressed Login");
        invaliduserTxt.setText("");
        if (isValidInput()) {
            try {
                LoginRequest loginReq = new LoginRequest();
                Credentials credentials = new Credentials(UserNameTxt.getText(), PasswordTxt.getText());
                loginReq.setCredentials(credentials);
                String jRequest = TickTackToeClient.mapper.writeValueAsString(loginReq);
                ServerListener.sendRequest(jRequest);
                UserNameTxt.clear();
                PasswordTxt.clear();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        else {
            UserNameTxt.setText("");
            PasswordTxt.setText("");
            UserNameTxt.requestFocus();
        }
    }
    private boolean isValidInput() {
        String regex = "^[a-z]+([_.][a-z0-9]+)*${4,}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher1 = pattern.matcher(UserNameTxt.getText().trim());
        if (UserNameTxt.getText().trim().equals("") || !matcher1.matches()) {
            invaliduserTxt.setText("Invalid username!");
            return false;
        }

        if (PasswordTxt.getText().equals("") || PasswordTxt.getText().length() < 3) {
            invaliduserTxt.setText("Invalid Password!");
            return false;
        }
        return true;
    }
    
    @FXML protected void onActionRegister(ActionEvent event) {
        TickTackToeClient.openRegisterView();
        System.out.println("pressed Register");
    }
    @FXML protected void onActionGuest(ActionEvent event) {
         
        System.out.println("pressed Guest");
    }
    
    public void handleResponse(LoginResponse loginRes) {
        if (Objects.equals(loginRes.getStatus(), Response.STATUS_OK)) {
            TickTackToeClient.homeController.fromLogin(loginRes.getPlayerFullInfo(), loginRes.getPlayerFullInfoMap());
            if (TickTackToeClient.openedScene != TickTackToeClient.scenes.homeS && TickTackToeClient.openedScene != TickTackToeClient.scenes.vsComputerS) {
                TickTackToeClient.openHomeView();
            } else {
                TickTackToeClient.showSystemNotification("Back Online", "You are now online", TrayIcon.MessageType.INFO);
            }
        } else {
            invaliduserTxt.setText(loginRes.getMessage());
        }
    }
    
   

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}
