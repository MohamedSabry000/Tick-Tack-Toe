/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.responses;


import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.server.models.Position;

/**
 *
 * @author wwwmo
 */
public class AskToPauseResponse extends Response {

    public AskToPauseResponse() {
        this.type = RESPONSE_ASK_TO_PAUSE;
    }

    public AskToPauseResponse(@JsonProperty("status") String status, @JsonProperty("type") String type, @JsonProperty("message") String message) {
        this.message = message;
        this.type = type;
        this.status = status;
    }
}
