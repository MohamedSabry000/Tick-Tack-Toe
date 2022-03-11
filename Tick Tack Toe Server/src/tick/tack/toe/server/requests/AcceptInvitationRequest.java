/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.server.models.Player;

/**
 *
 * @author wwwmo
 */
public class AcceptInvitationRequest extends Request {
    private Player player;

    public AcceptInvitationRequest() {
        super(ACTION_ACCEPT_INVITATION);
    }

    public AcceptInvitationRequest(Player player) {
        super(ACTION_ACCEPT_INVITATION);
        this.player = player;
    }

    public AcceptInvitationRequest(@JsonProperty("action") String action,
                               @JsonProperty("player") Player player) {
        super(action);
        this.player = player;
    }


    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
