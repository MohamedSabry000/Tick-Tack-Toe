/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author wwwmo
 */
public class AskToPauseRequest extends Request{
    public AskToPauseRequest() {
        super(ACTION_ASK_TO_PAUSE);
    }

    public AskToPauseRequest(@JsonProperty String action) {
        super(action);
    }
}
