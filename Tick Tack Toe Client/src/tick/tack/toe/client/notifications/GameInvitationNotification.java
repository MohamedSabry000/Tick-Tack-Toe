/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.client.models.Player;

/**
 *
 * @author wwwmo
 */
public class GameInvitationNotification extends Notification {
    private Player player;

    public GameInvitationNotification() {
        type = NOTIFICATION_GAME_INVITATION;
    }

    public GameInvitationNotification(Player player) {
        this.player = player;
        type = NOTIFICATION_GAME_INVITATION;
    }

    public GameInvitationNotification(@JsonProperty("player") Player player, @JsonProperty("type") String type) {
        this.type = type;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
