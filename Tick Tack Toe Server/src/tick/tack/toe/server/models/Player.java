/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.models;


/**
 *
 * @author booga
 */
public class Player {
    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    protected int player_id;
    protected long s_id; //to be known
    
    //must exist.
    public Player()
    {
    }
    public Player(PlayerFullInfo playerfullinfo)
    {
        this.s_id = playerfullinfo.s_id;
        this.player_id = playerfullinfo.player_id;
    }

    public Player(int database_id, long player_id) {
        this.player_id = database_id;
        this.s_id = player_id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public long getS_id() {
        return s_id;
    }

    public void setS_id(long s_id) {
        this.s_id = s_id;
    }

    
}
