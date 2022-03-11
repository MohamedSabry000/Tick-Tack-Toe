/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.server.models.Message;

/**
 *
 * @author wwwmo
 */

public class SendMessageRequest extends Request {
    private Message message;

    public SendMessageRequest() {
        super(ACTION_SEND_MESSAGE);
    }

    public SendMessageRequest(Message message) {
        super(ACTION_SEND_MESSAGE);
        this.message = message;
    }

    public SendMessageRequest(@JsonProperty("action") String action, @JsonProperty("message") Message message) {
        super(action);
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
