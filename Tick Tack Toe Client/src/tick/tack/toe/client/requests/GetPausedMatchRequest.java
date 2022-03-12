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
public class GetPausedMatchRequest extends Request{
    private int m_id;

    public GetPausedMatchRequest(){super(ACTION_GET_PAUSED_MATCH);}

    public GetPausedMatchRequest(int m_id) {
        super(ACTION_GET_PAUSED_MATCH);
        this.m_id = m_id;

    }

    public GetPausedMatchRequest(@JsonProperty("action") String action,
                             @JsonProperty("m_id") int m_id){
        super(ACTION_GET_PAUSED_MATCH);
        this.m_id = m_id;

    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }


}
