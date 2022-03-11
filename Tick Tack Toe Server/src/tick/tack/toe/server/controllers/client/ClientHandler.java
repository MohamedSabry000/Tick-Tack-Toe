/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.server.controllers.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import tick.tack.toe.server.controllers.db.*;
//import tick.tack.toe.server.models.*;


import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import tick.tack.toe.server.models.PlayerFullInfo;
import tick.tack.toe.server.controllers.MainViewController;
import tick.tack.toe.server.requests.*;
import tick.tack.toe.server.responses.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import tick.tack.toe.server.TickTackToeServer;
import tick.tack.toe.server.controllers.MainViewController;
import tick.tack.toe.server.notifications.*;
import org.json.JSONObject;
import tick.tack.toe.server.models.Player;


/**
 *
 * @author wwwmo
 */
public class ClientHandler extends Thread {
    private static final DBConnection dbConnection = new DBConnection();
    private Socket mySocket;
    
    private BufferedReader dataInputStream;
    private PrintStream printStream;
    private static Map<Long, ClientHandler> clients = new HashMap<>();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static Map<Integer, PlayerFullInfo> playersFullInfo;
    private Map<String, IAction> actions;
    private ClientHandler competitor;
    private PlayerFullInfo myFullInfoPlayer;


    
    public ClientHandler(Socket socket) {
        initActions();
        try {
            dataInputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            clients.put(this.getId(), this);
            System.out.println("No. Clients: " + clients.size());
            mySocket = socket;
            start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void initActions() {
        actions = new HashMap<>();
//        actions.put(Request.ACTION_INVITE_TO_GAME, this::inviteToGame);
//        actions.put(Request.ACTION_ACCEPT_INVITATION, this::acceptInvitation);
//        actions.put(Request.ACTION_REJECT_INVITATION, this::rejectInvitation);
//        actions.put(Request.ACTION_UPDATE_BOARD, this::updateBoard);
//        actions.put(Request.ACTION_UPDATE_IN_GAME_STATUS, this::updateInGameStatus);
        actions.put(Request.ACTION_SIGN_UP, this::signUp);
        actions.put(Request.ACTION_LOGIN, this::login);
//        actions.put(Request.ACTION_ASK_TO_PAUSE, this::askToPause);
//        actions.put(Request.ACTION_SAVE_MATCH, this::saveMatch);
//        actions.put(Request.ACTION_REJECT_TO_PAUSE, this::rejectToPause);
//        actions.put(Request.ACTION_SEND_MESSAGE, this::sendMessage);
//        actions.put(Request.ACTION_GET_MATCH_HISTORY, this::getMatchHistory);
//        actions.put(Request.ACTION_ASK_TO_RESUME, this::askToResume);
//        actions.put(Request.ACTION_REJECT_TO_RESUME, this::rejectToResume);
//        actions.put(Request.ACTION_ACCEPT_TO_RESUME, this::acceptToResume);
//        actions.put(Request.ACTION_BACK_FROM_OFFLINE, this::backFromOffline);
//        actions.put(Request.ACTION_GET_PAUSED_MATCH, this::getPausedMatch);
    }
    public static void initPlayerList() {
        playersFullInfo = new HashMap<>();
        playersFullInfo = DBConnection.getAllPlayers();
        System.out.println(playersFullInfo.size());
        Platform.runLater(() -> MainViewController.fillPlayersTable(playersFullInfo.values()));
    }
    public static PlayerFullInfo getPlayerFullInfo(int player_id) {
        return playersFullInfo.get(player_id);
    }

    @Override
    public void run() {
        while (true) {  
            try {
                String jRequest = dataInputStream.readLine();
                System.out.println(jRequest);
                JSONObject json = new JSONObject(jRequest);
                String clientAction = (String) json.get("action");
                actions.get(clientAction).handleAction(jRequest);
            } catch (Exception e) {
                System.out.println("Stopped");
                dropConnection();
                System.out.println("No. of Clients: " + clients.size());
                e.printStackTrace();
                break;
            }
        }
    }
    private void dropConnection() {
        // check if were playing with a competitor
        if (clients.get(this.getId()).competitor != null) {
            try {
                // notify the competitor
                CompetitorConnectionIssueNotification competitorConnectionIssueNotification = new CompetitorConnectionIssueNotification();
                String jNotification = mapper.writeValueAsString(competitorConnectionIssueNotification);

                clients.get(this.getId()).competitor.printStream.println(jNotification);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        // if not logged in
        if (clients.get(this.getId()).myFullInfoPlayer != null) {
            clients.get(this.getId()).myFullInfoPlayer.setStatus(PlayerFullInfo.OFFLINE);
            clients.get(this.getId()).myFullInfoPlayer.setInGame(false);
            clients.get(this.getId()).myFullInfoPlayer.setServer_id(-1);
        }
        updateStatus(clients.get(this.getId()).myFullInfoPlayer);
        clients.remove(this.getId());
    }
    
    public static void stopAll() {
        for (ClientHandler client : clients.values()) {
            client.interrupt();
        }
    }
    
    @Override
    public void interrupt() {
        super.interrupt();
        try {
            mySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void login(String json) {
        try {
            LoginRequest loginReq = mapper.readValue(json, LoginRequest.class);
            LoginResponse loginRes = new LoginResponse();
            int u_id = dbConnection.authenticate(loginReq.getCredentials());
            if (u_id != -1) {
                System.out.println(u_id);
                playersFullInfo.get(u_id).setStatus(PlayerFullInfo.ONLINE);
                playersFullInfo.get(u_id).setServer_id(this.getId());
                // playersFullInfo <database_id, PlayerFullInfo>
                // clients <server_id, PlayerFullInfo>
                clients.get(this.getId()).myFullInfoPlayer = playersFullInfo.get(u_id);
                updateStatus(clients.get(this.getId()).myFullInfoPlayer);
                
                loginRes.setStatus(LoginResponse.STATUS_OK);
                loginRes.setPlayerFullInfo(playersFullInfo.get(u_id));
                loginRes.setPlayerFullInfoMap(playersFullInfo);
            } else {
                loginRes.setStatus(LoginResponse.STATUS_ERROR);
                loginRes.setMessage("Incorrect Password or Username.");
            }
            String jResponse = mapper.writeValueAsString(loginRes);
            System.out.println(jResponse);
            printStream.println(jResponse);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void updateStatus(PlayerFullInfo playerFullInfo) {
        // update player status in the list
//        playersFullInfo.put(playerFullInfo.getServer_id(), playerFullInfo);
        // create notification to send
        UpdateStatusNotification updateStatusNotification = new UpdateStatusNotification(playerFullInfo);
        try {
            // create json from the notification
            String jNotification = mapper.writeValueAsString(updateStatusNotification);
            // send to all client notification with now status for the player
            new Thread(() -> {
                for (ClientHandler client : clients.values()) {
                    if (client.myFullInfoPlayer != null) {
                        client.printStream.println(jNotification);
                        System.out.println("Notificaion: -> "+jNotification.toString());
                    }
                }
            }).start();
            Platform.runLater(() -> TickTackToeServer.controller.fillPlayersTable(playersFullInfo.values()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void signUp(String json) {
        try {
            SignUpRequest signUpReq = mapper.readValue(json, SignUpRequest.class);
            PlayerFullInfo playerFullInfo = dbConnection.signUp(signUpReq.getUser());
            SignUpResponse signUpRes = new SignUpResponse();
            if (playerFullInfo != null) {
                signUpRes.setStatus(Response.STATUS_OK);
                signUpRes.setMessage("You have successfully registered");
                int lastPlayerId = -1;
                do{
                    lastPlayerId = dbConnection.getTheLastPlayerId();
                    if (lastPlayerId != -1) {
                        playersFullInfo.put(lastPlayerId, playerFullInfo);
                        updateStatus(playerFullInfo);
                    }
                } while(lastPlayerId == -1);
                 
                
            } else {
                signUpRes.setStatus(Response.STATUS_ERROR);
                signUpRes.setMessage("Username You entered already exists!");
            }
            String jResponse = mapper.writeValueAsString(signUpRes);
            printStream.println(jResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void updateInGameStatus(String json) {
        try {
            UpdateInGameStatusRequest updateInGameStatusReq = mapper.readValue(json, UpdateInGameStatusRequest.class);
            clients.get(this.getId()).myFullInfoPlayer.setInGame(updateInGameStatusReq.getInGame());
            updateStatus(clients.get(this.getId()).myFullInfoPlayer);
            // if were in game then competitor disconnected
            if (!updateInGameStatusReq.getInGame() && clients.get(this.getId()).competitor != null) {
                clients.get(this.getId()).competitor = null;
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void inviteToGame(String json) {
        try {
            InviteToGameRequest inviteToGameReq = mapper.readValue(json, InviteToGameRequest.class);
            // get the competitor and send the notification
            ClientHandler _competitor = clients.get(inviteToGameReq.getPlayer().getServer_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // create the notification
                GameInvitationNotification gameInvitationNotification = new GameInvitationNotification(new Player(clients.get(this.getId()).myFullInfoPlayer));
                // create json from the notification
                String jNotification = mapper.writeValueAsString(gameInvitationNotification);
                // send the game invitation to the competitor
                _competitor.printStream.println(jNotification);
            } else {
                // the competitor became offline or started a new another game
                sendOfflineOrInGame(inviteToGameReq.getPlayer(), _competitor);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void sendOfflineOrInGame(Player player, ClientHandler _competitor) throws JsonProcessingException {
        // create error response
        InviteToGameResponse inviteToGameRes = new InviteToGameResponse(InviteToGameResponse.STATUS_ERROR, player);
        if (_competitor == null) {
            inviteToGameRes.setMessage("Your competitor became offline now.");
        } else {
            inviteToGameRes.setMessage("It seems your competitor has entered another game.");
        }
        // create json from response
        String jResponse = mapper.writeValueAsString(inviteToGameRes);
        // send the response to the client
        printStream.println(jResponse);
    }
    
    
    interface IAction {
        void handleAction(String json);
    }
}
