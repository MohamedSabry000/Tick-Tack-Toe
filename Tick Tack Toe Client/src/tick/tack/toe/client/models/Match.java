/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.models;

//@JsonFormat is defined in the jackson-databind package so we need the following Maven Dependency:
/*
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.13.0</version>
</dependency>
*/
//import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;
/**
 *
 * @author booga
 */
public class Match {
    private int match_id,player1_id,player2_id,winner;
    private String player1_choice, player2_choice, status, level;
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Timestamp match_date;
    
    public static final char CHOICE_X = 'X';
    public static final char CHOICE_O = 'O';
    public static final String STATUS_FINISHED = "finished";
    public static final String STATUS_PAUSED = "paused";
    public static String GAME_GRID = "---------";

    public Match() {
        this.match_id = this.winner = -1;
    }
    
      public Match(int player1_id, int player2_id, int winner, String player1_choice, String player2_choice, String status, Timestamp match_date) {
        this.player1_id = player1_id;
        this.player2_id = player2_id;
        this.winner = winner;
        this.player1_choice = player1_choice;
        this.player2_choice = player2_choice;
        this.status = status;
        this.match_date = match_date;
    }

    public Match(int player1_id, int player2_id, String player1_choice, String player2_choice, String status, Timestamp match_date) {
        this.player1_id = player1_id;
        this.player2_id = player2_id;
        this.player1_choice = player1_choice;
        this.player2_choice = player2_choice;
        this.status = status;
        this.match_date = match_date;
        this.match_id = this.winner = -1;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public int getPlayer1_id() {
        return player1_id;
    }

    public void setPlayer1_id(int player1_id) {
        this.player1_id = player1_id;
    }

    public int getPlayer2_id() {
        return player2_id;
    }

    public static String getGAME_GRID() {
        return GAME_GRID;
    }

     public void setGAME_GRID(String game_grid) {
        GAME_GRID = game_grid;
    }
    public void setPlayer2_id(int player2_id) {
        this.player2_id = player2_id;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public String getPlayer1_choice() {
        return player1_choice;
    }

    public void setPlayer1_choice(String player1_choice) {
        this.player1_choice = player1_choice;
    }

    public String getPlayer2_choice() {
        return player2_choice;
    }

    public void setPlayer2_choice(String player2_choice) {
        this.player2_choice = player2_choice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Timestamp getMatch_date() {
        return match_date;
    }

    public void setMatch_date(Timestamp match_date) {
        this.match_date = match_date;
    }

    
}
