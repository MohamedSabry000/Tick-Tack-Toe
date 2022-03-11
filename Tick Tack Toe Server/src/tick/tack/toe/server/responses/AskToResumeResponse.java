/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.server.models.Player;

/**
 *
 * @author wwwmo
 */
public class AskToResumeResponse extends Response {
    private Player player;

    public AskToResumeResponse(String status, Player player) {
        //super(status,RESPONSE_INVITE_TO_GAME);
        this.player = player;
        this.status = status;
        this.type = RESPONSE_ASK_TO_RESUME;

    }

    public AskToResumeResponse(String status, String message, Player player) {
        //super(status,RESPONSE_INVITE_TO_GAME);
        this.player = player;
        this.status = status;
        this.message = message;
        this.type = RESPONSE_ASK_TO_RESUME;
    }

    public AskToResumeResponse(@JsonProperty("status") String status,
                           @JsonProperty("type") String type,
                           @JsonProperty("message") String message,
                           @JsonProperty("player") Player player) {
        //super(message,status,RESPONSE_INVITE_TO_GAME);
        this.status = status;
        this.type = type;
        this.message = message;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
