/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.server.models.Position;

/**
 *
 * @author wwwmo
 */

public class UpdateBoardRequest extends Request {
    private Position position;

    public UpdateBoardRequest() {
        super(ACTION_UPDATE_BOARD);
    }

    public UpdateBoardRequest(Position position) {
        super(ACTION_UPDATE_BOARD);
        this.position = position;
    }

    public UpdateBoardRequest(@JsonProperty("action") String action, @JsonProperty("position") Position position) {
        super(action);
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
