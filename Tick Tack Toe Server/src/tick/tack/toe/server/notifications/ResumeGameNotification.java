package tick.tack.toe.server.notifications;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import tick.tack.toe.server.models.Match;
import tick.tack.toe.server.models.Position;

public class ResumeGameNotification extends Notification {
    private Match match;
    private List<Position> positions;

    public ResumeGameNotification() {
        this.type = NOTIFICATION_RESUME_GAME;
    }

    public ResumeGameNotification(Match match, List<Position> positions) {
        this.match = match;
        this.positions = positions;
        this.type = NOTIFICATION_RESUME_GAME;

    }

    public ResumeGameNotification(@JsonProperty("type") String type,
                                  @JsonProperty("match") Match match,
                                  @JsonProperty("positions") List<Position> positions) {
        super(type);
        this.match = match;
        this.positions = positions;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }
}
