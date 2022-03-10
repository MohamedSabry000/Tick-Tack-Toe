/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author wwwmo
 */
public class SignUpResponse extends Response {
    public SignUpResponse() {
        this.type = RESPONSE_SIGN_UP;
    }

    public SignUpResponse(@JsonProperty("message") String message,
                     @JsonProperty("status") String status,
                     @JsonProperty("type") String type) {
        super(message, status, type);
    }
}
