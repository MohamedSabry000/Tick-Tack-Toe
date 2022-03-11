/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.client.models.PlayerFullInfo;

/**
 *
 * @author wwwmo
 */
public class UpdateStatusNotification extends Notification {
    PlayerFullInfo playerFullInfo;

    public UpdateStatusNotification() {
        type = NOTIFICATION_UPDATE_STATUS;
    }

    public UpdateStatusNotification(PlayerFullInfo playerFullInfo) {
        this.playerFullInfo = playerFullInfo;
        type = NOTIFICATION_UPDATE_STATUS;
    }

    public UpdateStatusNotification(@JsonProperty("playerFullInfo") PlayerFullInfo playerFullInfo,
                                    @JsonProperty("type") String type) {
        this.playerFullInfo = playerFullInfo;
        this.type = type;
    }

    public PlayerFullInfo getPlayerFullInfo() {
        return playerFullInfo;
    }

    public void setPlayerFullInfo(PlayerFullInfo playerFullInfo) {
        this.playerFullInfo = playerFullInfo;
    }
}
