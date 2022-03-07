/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.models;


/**
 *
 * @author booga
 */
public class Player {
    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    protected int match_id;
    protected long player_id;
    
    //must exist.
    public Player()
    {
    }
    public Player(PlayerFullInfo playerfullinfo)
    {
        this.player_id = playerfullinfo.player_id;
        this.match_id = playerfullinfo.match_id;
    }

    public Player(int database_id, long player_id) {
        this.match_id = database_id;
        this.player_id = player_id;
    }

    public int getMatch_id() {
        return match_id;
    }

    public void setMatch_id(int match_id) {
        this.match_id = match_id;
    }

    public long getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(long player_id) {
        this.player_id = player_id;
    }
    
}