/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.client.models.Player;

/**
 *
 * @author wwwmo
 */
public class InviteToGameRequest extends Request {
    private Player player;

    public InviteToGameRequest() {
        super(ACTION_INVITE_TO_GAME);
    }

    public InviteToGameRequest(Player player) {
        super(ACTION_INVITE_TO_GAME);
        this.player = player;
    }

    public InviteToGameRequest(@JsonProperty("player") Player player, @JsonProperty("action") String action) {
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
