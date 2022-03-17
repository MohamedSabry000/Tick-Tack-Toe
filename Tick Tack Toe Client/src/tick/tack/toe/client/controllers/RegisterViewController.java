/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import tick.tack.toe.client.TickTackToeClient;
import static tick.tack.toe.client.TickTackToeClient.mapper;
import tick.tack.toe.client.controllers.server.ServerListener;
import tick.tack.toe.client.models.User;
import tick.tack.toe.client.requests.SignUpRequest;
import tick.tack.toe.client.responses.Response;


// import tick.tack.toe.server.controllers.client.*;


public class RegisterViewController implements Initializable{
    @FXML private TextField FirstNameTxt;
    @FXML private PasswordField PasswordTxt;
    @FXML private TextField UserNameTxt;
    @FXML private Label invalidinput;
    
    String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    /**
     * ^                 # start-of-string
       (?=.*[0-9])       # a digit must occur at least once
       (?=.*[a-z])       # a lower case letter must occur at least once
       (?=.*[A-Z])       # an upper case letter must occur at least once
       (?=.*[@#$%^&+=])  # a special character must occur at least once
       (?=\S+$)          # no whitespace allowed in the entire string
       .{8,}             # anything, at least eight places though
       $                 # end-of-string
     */
    Pattern pattern = Pattern.compile(regex);
    
    @FXML protected void onActionRegister(ActionEvent event) {
        invalidinput.setText("");
        if (isValidInput()) {
            User user = new User(UserNameTxt.getText().trim(), PasswordTxt.getText());
            user.setName(FirstNameTxt.getText().trim());
            SignUpRequest signUpReq = new SignUpRequest();
            signUpReq.setUser(user);
            try {
                String jRequest = mapper.writeValueAsString(signUpReq);
                ServerListener.sendRequest(jRequest);
                UserNameTxt.clear();
                PasswordTxt.clear();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("not validated !");
        }
        System.out.println("pressed Register");
    }
    private boolean isValidInput() {
        Matcher matcher1 = pattern.matcher(PasswordTxt.getText().trim());
        if (UserNameTxt.getText().trim().equals("") || UserNameTxt.getText().length() < 4 || UserNameTxt.getText().contains(" ")) {
            invalidinput.setText("Invalid username!");
            return false;
        }
        if (FirstNameTxt.getText().trim().equals("")) {
            invalidinput.setText("Invalid name!");
            return false;
        }
        if (PasswordTxt.getText().equals("") || !matcher1.matches() ) {
            invalidinput.setText("Invalid Password!");
            return false;
        }
        return true;
    }
    
    @FXML protected void onActionBack(ActionEvent event) {
        TickTackToeClient.openLoginView();
        System.out.println("pressed Back");
    }

    public void handleResponse(Response signUpRes) {
        if (Objects.equals(signUpRes.getStatus(), Response.STATUS_OK)) {
            TickTackToeClient.openLoginView();
        } else {
            //TickTackToeClient.openRegisterView();
            invalidinput.setText("Player Name Already Exits.");
        }
    }
    @FXML protected void onActionCloseBtn (ActionEvent event){
        Platform.exit();
    }
    @FXML protected void onActionMinBtn (ActionEvent event){
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    
    @Override
    public void initialize(URL location, ResourceBundle resources) { }
    
}

