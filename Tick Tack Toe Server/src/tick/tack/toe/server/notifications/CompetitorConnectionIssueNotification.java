/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author wwwmo
 */
public class CompetitorConnectionIssueNotification extends Notification {
    public CompetitorConnectionIssueNotification() {
        this.type = NOTIFICATION_COMPETITOR_CONNECTION_ISSUE;
    }

    public CompetitorConnectionIssueNotification(@JsonProperty("type") String type) {
        super(type);
    }
}
