package tick.tack.toe.client.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateInGameStatusRequest extends Request {
    private boolean inGame;

    public UpdateInGameStatusRequest() {

        super(ACTION_UPDATE_IN_GAME_STATUS);
    }

    public UpdateInGameStatusRequest(boolean inGame) {
        this.inGame = inGame;
        this.action = ACTION_UPDATE_IN_GAME_STATUS;
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