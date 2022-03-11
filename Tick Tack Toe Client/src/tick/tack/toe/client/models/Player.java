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
    protected int db_player_id;
    protected long server_player_id;
    
    //must exist.
    public Player() {  }
    public Player(PlayerFullInfo playerfullinfo)
    {
        this.db_player_id = playerfullinfo.db_player_id;
        this.server_player_id = playerfullinfo.server_player_id;
    }

    public Player(int database_id, long s_id) {
        this.server_player_id = s_id;
        this.db_player_id = database_id;
    }

    public long getServer_id() {
        return server_player_id;
    }

    public void setServer_id(long s_id) {
        this.server_player_id = s_id;
    }

    public int getDb_Player_id() {
        return db_player_id;
    }

    public void setDb_Player_id(int player_id) {
        this.db_player_id = player_id;
    }
    
}
