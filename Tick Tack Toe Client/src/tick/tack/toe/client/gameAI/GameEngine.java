/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.gameAI;

import java.util.Map;
import javafx.scene.control.Button;


/**
 *
 * @author booga
 */
public class GameEngine {
    
    public boolean checkWinner(String choice, Map<String, Button> buttons) {
        
        for (int a = 0; a < 9; a++) {
            String line="";
            switch (a) 
            {
                case 0 : line = buttons.get("b1").getText() + buttons.get("b2").getText() + buttons.get("b3").getText();
                    System.out.println(line);
                break;
                case 1 : line = buttons.get("b4").getText() + buttons.get("b5").getText() + buttons.get("b6").getText();
                System.out.println(line);
                break;
                case 2 : line = buttons.get("b7").getText() + buttons.get("b8").getText() + buttons.get("b9").getText();
                System.out.println(line);
                break;
                case 3 : line = buttons.get("b1").getText() + buttons.get("b5").getText() + buttons.get("b9").getText();
                System.out.println(line);
                break;
                case 4 : line = buttons.get("b3").getText() + buttons.get("b5").getText() + buttons.get("b7").getText();
                System.out.println(line);
                break;
                case 5 : line = buttons.get("b1").getText() + buttons.get("b4").getText() + buttons.get("b7").getText();
                System.out.println(line);
                break;
                case 6 : line = buttons.get("b2").getText() + buttons.get("b5").getText() + buttons.get("b8").getText();
                System.out.println(line);
                break;
                case 7 : line = buttons.get("b3").getText() + buttons.get("b6").getText() + buttons.get("b9").getText();
                System.out.println(line);
                break;
                case 8 : line =  buttons.get("b2").getText() + buttons.get("b8").getText() + buttons.get("b5").getText();
                default : line="";
                
               
            }
            if (line.equals(choice + choice + choice)) {
                return true;
            } 
        }
        return false;
    }

}

