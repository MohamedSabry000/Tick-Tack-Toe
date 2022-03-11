/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import tick.tack.toe.client.models.MatchTable;

/**
 *
 * @author wwwmo
 */
public class GetMatchHistoryResponse extends Response{
    private List<MatchTable> matches;

    public GetMatchHistoryResponse() {
        this.type = RESPONSE_GET_MATCH_HISTORY;
    }

    public GetMatchHistoryResponse(String status,  List<MatchTable> matches) {
        this.matches = matches;
        this.type = RESPONSE_GET_MATCH_HISTORY;
        this.status = status;
    }

    public GetMatchHistoryResponse(@JsonProperty("status") String status,
                              @JsonProperty("type") String type,
                              @JsonProperty("message") String message,
                              @JsonProperty("matches") List<MatchTable> matches) {
        this.matches = matches;
        this.type = RESPONSE_GET_MATCH_HISTORY;
        this.status = status;
        this.message=message;
    }


    public List<MatchTable> getMatches() {
        return matches;
    }

    public void setMatches(List<MatchTable> matches) {
        this.matches = matches;
    }
}
