/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import tick.tack.toe.client.models.Position;
import tick.tack.toe.client.models.Match;

/**
 *
 * @author wwwmo
 */
public class GetPausedMatchResponse extends Response{
    private List<Position> positions;
    private Match match;

    public GetPausedMatchResponse(){super(RESPONSE_GET_PAUSED_MATCH);
        this.match = match;
    }

    public GetPausedMatchResponse(List<Position> positions, Match match) {
        super(RESPONSE_GET_PAUSED_MATCH);
        this.positions = positions;
        this.match=match;
    }

    public GetPausedMatchResponse(@JsonProperty("type") String type,
                             @JsonProperty("positions") List<Position> positions,
                             @JsonProperty("match") Match match){
        super(RESPONSE_GET_PAUSED_MATCH);
        this.positions=positions;
        this.match=match;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
