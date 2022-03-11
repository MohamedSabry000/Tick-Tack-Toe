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

public class AcceptToPauseRequest extends Request{
    public AcceptToPauseRequest() {
        super(ACTION_ACCEPT_TO_PAUSE);
    }

    public AcceptToPauseRequest(@JsonProperty String action) {
        super(action);
    }
}
