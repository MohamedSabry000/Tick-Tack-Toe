/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.server.models.Credentials;

/**
 *
 * @author wwwmo
 */
public class LoginRequest extends Request{
    private Credentials credentials;

    public LoginRequest() {
        super(ACTION_LOGIN);
    }

    public LoginRequest(Credentials credentials) {
        super(ACTION_LOGIN);
        this.credentials = credentials;
    }

    public LoginRequest(@JsonProperty("action") String action,
                        @JsonProperty("credentials") Credentials credentials) {
        super(ACTION_LOGIN);
        this.credentials = credentials;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
}
