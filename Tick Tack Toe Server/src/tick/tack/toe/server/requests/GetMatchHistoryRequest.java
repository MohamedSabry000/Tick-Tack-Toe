/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.requests;

/**
 *
 * @author wwwmo
 */
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetMatchHistoryRequest extends Request{
    public GetMatchHistoryRequest() {
        super(ACTION_GET_MATCH_HISTORY);
    }

    public GetMatchHistoryRequest(@JsonProperty String action) {
        super(ACTION_GET_MATCH_HISTORY);
    }
}
