/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.server.models.Position;
/**
 *
 * @author wwwmo
 */
public class UpdateBoardNotification extends Notification {
    private Position position;

    public UpdateBoardNotification() {
        this.type = NOTIFICATION_UPDATE_BOARD;
    }

    public UpdateBoardNotification(Position position) {
        this.position = position;
        this.type = NOTIFICATION_UPDATE_BOARD;
    }

    public UpdateBoardNotification(@JsonProperty("type") String type, @JsonProperty("position") Position position) {
        this.position = position;
        this.type = type;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
