/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author wwwmo
 */
public class UpdateInGameStatusRequest extends Request {
    private boolean inGame;

    public UpdateInGameStatusRequest() {
        super(ACTION_UPDATE_IN_GAME_STATUS);
    }

    public UpdateInGameStatusRequest(boolean inGame) {
        this.inGame = inGame;
    }

    public UpdateInGameStatusRequest(@JsonProperty("action") String action,
                                 @JsonProperty("inGame") boolean inGame) {
        super(action);
        this.inGame = inGame;
    }

    public boolean getInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }
}
