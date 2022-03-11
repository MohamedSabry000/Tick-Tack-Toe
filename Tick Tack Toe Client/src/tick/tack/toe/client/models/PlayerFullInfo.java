/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author booga
 */
public class PlayerFullInfo extends Player{
    private SimpleIntegerProperty points;
    private SimpleStringProperty name, status;
    private boolean inGame;


  
    public PlayerFullInfo(){
       db_player_id = -1;
        this.name = new SimpleStringProperty("");
       this.points = new SimpleIntegerProperty(0);
       status = new SimpleStringProperty(OFFLINE);
       inGame = false;
    }

    public PlayerFullInfo(int index, int player_id, String name, int points) {
        this.db_player_id = player_id;
        this.name = new SimpleStringProperty(name);
        this.points = new SimpleIntegerProperty(points);
        status = new SimpleStringProperty(OFFLINE);
        inGame = false;
    }

    public PlayerFullInfo(int player_id, String name, int points) {
        this.db_player_id = player_id;
        this.name = new SimpleStringProperty(name);
        this.points = new SimpleIntegerProperty(points);
        status = new SimpleStringProperty(OFFLINE);
        inGame = false;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status = new SimpleStringProperty(status);
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public int getPoints() {
        return points.get();
    }

    public void setPoints(int points) {
        this.points = new SimpleIntegerProperty(points);

        
    }
}
