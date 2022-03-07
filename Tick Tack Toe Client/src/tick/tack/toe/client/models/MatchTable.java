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
public class MatchTable {
    private String player1_Name, player2_Name, winner,status;
    private String match_date;
    private int match_id,player1_id, player2_id;
    public static final String STATUS_FINISHED = "finished";
    public static final String STATUS_PAUSED = "paused";

    public MatchTable(String player1_Name, String player2_Name, String winner, String status, String match_date, int match_id, int player1_id, int player2_id) {
        this.player1_Name = player1_Name;
        this.player2_Name = player2_Name;
        this.status = status;
        this.match_date = match_date;
        this.winner=winner;
        this.player1_id = player1_id;
        this.player2_id = player2_id;
    }
    public String getPlayer1_Name() {
        return player1_Name;
    }

    public void setPlayer1_Name(String player1_Name) {
        this.player1_Name = player1_Name;
    }

    public String getPlayer2_Name() {
        return player2_Name;
    }

    public void setPlayer2_Name(String player2_Name) {
        this.player2_Name = player2_Name;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatch_date() {
        return match_date;
    }

    public void setMatch_date(String match_date) {
        this.match_date = match_date;
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

    public void setPlayer2_id(int player2_id) {
        this.player2_id = player2_id;
    }

    
}