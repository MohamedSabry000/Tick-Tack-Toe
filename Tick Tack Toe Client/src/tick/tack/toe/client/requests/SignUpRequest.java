/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import tick.tack.toe.client.models.User;

/**
 *
 * @author wwwmo
 */
public class SignUpRequest extends Request {
    private User user;

    public SignUpRequest() {
        super(ACTION_SIGN_UP);
    }

    public SignUpRequest(User user) {
        super(ACTION_SIGN_UP);
        this.user = user;
    }

    public SignUpRequest(@JsonProperty("user") User user, @JsonProperty("action") String action) {
        super(ACTION_SIGN_UP);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}