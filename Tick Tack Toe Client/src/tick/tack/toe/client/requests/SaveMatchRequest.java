/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import tick.tack.toe.client.models.Match;
import tick.tack.toe.client.models.Position;
/**
 *
 * @author wwwmo
 */
public class SaveMatchRequest extends Request {
    private Match match;
    private List<Position> positions;

    public SaveMatchRequest() {
        this.action = ACTION_SAVE_MATCH;
    }

    public SaveMatchRequest(Match match, List<Position> positions) {
        this.action = ACTION_SAVE_MATCH;
        this.match = match;
        this.positions = positions;
    }

    public SaveMatchRequest(@JsonProperty("action") String action,
                        @JsonProperty("match") Match match,
                        @JsonProperty("positions") List<Position> positions) {
        super(action);
        this.match = match;
        this.positions = positions;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}

