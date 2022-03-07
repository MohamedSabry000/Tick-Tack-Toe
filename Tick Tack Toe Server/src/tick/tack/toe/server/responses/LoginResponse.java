/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import tick.tack.toe.server.models.*;
/**
 *
 * @author wwwmo
 */
public class LoginResponse extends Response{
    private PlayerFullInfo playerFullInfo;
    private Map<Integer,PlayerFullInfo> playerFullInfoMap;

    public LoginResponse(){
        this.type = RESPONSE_LOGIN;
    }

    public LoginResponse(String status) {
        this.status = status;
    }



    public LoginResponse(String status, PlayerFullInfo playerFullInfo, Map<Integer,PlayerFullInfo> playerFullInfoMap) {
        this.playerFullInfo = playerFullInfo;
        this.status = status;
        this.type = RESPONSE_LOGIN;
        this.playerFullInfoMap=playerFullInfoMap;
    }


    public LoginResponse(String status, String message, PlayerFullInfo playerFullInfo) {
        this.playerFullInfo = playerFullInfo;
        this.status = status;
        this.message = message;
        this.type = RESPONSE_LOGIN;

    }

    public LoginResponse(@JsonProperty("status") String status,
                           @JsonProperty("type") String type,
                           @JsonProperty("message") String message,
                           @JsonProperty("playerFullInfo") PlayerFullInfo playerFullInfo,
                           @JsonProperty("playerFullInfoMap") Map<Integer,PlayerFullInfo> playerFullInfoMap) {
        this.status = status;
        this.type = type;
        this.message = message;
        this.playerFullInfo = playerFullInfo;
        this.playerFullInfoMap = playerFullInfoMap;

    }

    public PlayerFullInfo getPlayerFullInfo() {
        return playerFullInfo;
    }

    public void setPlayerFullInfo(PlayerFullInfo playerFullInfo) {
        this.playerFullInfo = playerFullInfo;
        this.playerFullInfoMap=playerFullInfoMap;
    }

    public Map<Integer, PlayerFullInfo> getPlayerFullInfoMap() {
        return playerFullInfoMap;
    }

    public void setPlayerFullInfoMap(Map<Integer, PlayerFullInfo> playerFullInfoMap) {
        this.playerFullInfoMap = playerFullInfoMap;
    }
}
