/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;



public class ScoreViewController {
     @FXML protected void onActionPlayAgain(ActionEvent event) {

        System.out.println("pressed Play Again");
    }
      @FXML protected void onActionBack(ActionEvent event) {

        System.out.println("pressed Back");
    }
  
}

