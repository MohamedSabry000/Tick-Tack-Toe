/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.models;

/**
 *
 * @author booga
 */
public class PlayerFullInfo extends Player{
    private int points;
    private String name, status;
    private boolean inGame;

  
    public PlayerFullInfo(){
       player_id = -1;
    }

    public PlayerFullInfo(int index, int database_id, String name, int points) {
        this.database_id = database_id;
        this.name = name;
        this.points = points;
        player_id = -1;
        status = OFFLINE;
        inGame = false;
    }

    public PlayerFullInfo(int database_id, String name, int points) {
        this.database_id = database_id;
        this.name = name;
        this.points = points;
        player_id = -1;
        status = OFFLINE;
        inGame = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
