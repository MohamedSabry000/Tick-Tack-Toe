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
import tick.tack.toe.server.models.Match;
import tick.tack.toe.server.models.MatchTable;
import tick.tack.toe.server.models.Player;
import tick.tack.toe.server.models.Position;


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
        actions.put(Request.ACTION_INVITE_TO_GAME, this::inviteToGame);
        actions.put(Request.ACTION_ACCEPT_INVITATION, this::acceptInvitation);
        actions.put(Request.ACTION_REJECT_INVITATION, this::rejectInvitation);
            actions.put(Request.ACTION_UPDATE_BOARD, this::updateBoard);
        actions.put(Request.ACTION_UPDATE_IN_GAME_STATUS, this::updateInGameStatus);
        actions.put(Request.ACTION_SIGN_UP, this::signUp);
        actions.put(Request.ACTION_LOGIN, this::login);
        actions.put(Request.ACTION_ASK_TO_PAUSE, this::askToPause);
        actions.put(Request.ACTION_SAVE_MATCH, this::saveMatch);
        actions.put(Request.ACTION_REJECT_TO_PAUSE, this::rejectToPause);
        actions.put(Request.ACTION_SEND_MESSAGE, this::sendMessage);
        actions.put(Request.ACTION_GET_MATCH_HISTORY, this::getMatchHistory);
        actions.put(Request.ACTION_ASK_TO_RESUME, this::askToResume);
        actions.put(Request.ACTION_REJECT_TO_RESUME, this::rejectToResume);
        actions.put(Request.ACTION_ACCEPT_TO_RESUME, this::acceptToResume);
        actions.put(Request.ACTION_BACK_FROM_OFFLINE, this::backFromOffline);
        actions.put(Request.ACTION_GET_PAUSED_MATCH, this::getPausedMatch);
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
            
            dbConnection.updatePlayerDBStatus(clients.get(this.getId()).myFullInfoPlayer.getDb_Player_id(), PlayerFullInfo.OFFLINE);
            
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
            System.out.println("i'm id: "+loginReq.getCredentials() );
            System.out.println("i'm id2: "+loginReq);
            if (u_id != -1) {
                System.out.println(u_id);
                playersFullInfo.get(u_id).setStatus(PlayerFullInfo.ONLINE);
                playersFullInfo.get(u_id).setServer_id(this.getId());
                // playersFullInfo <database_id, PlayerFullInfo>
                // clients <server_id, PlayerFullInfo>
                clients.get(this.getId()).myFullInfoPlayer = playersFullInfo.get(u_id);
                updateStatus(clients.get(this.getId()).myFullInfoPlayer);
                
                dbConnection.updatePlayerDBStatus(u_id, PlayerFullInfo.ONLINE);
                
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
            System.out.println("Client Handler -> inviteToGame: "+inviteToGameReq.getPlayer().getServer_id());
            System.out.println("Client Handler -> inviteToGame2: "+_competitor.getName());

            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // create the notification
                GameInvitationNotification gameInvitationNotification = new GameInvitationNotification(new Player(clients.get(this.getId()).myFullInfoPlayer));
                // create json from the notification
                String jNotification = mapper.writeValueAsString(gameInvitationNotification);
                // send the game invitation to the competitor
                _competitor.printStream.println(jNotification);
                System.out.println("Response to Invitation Notification: "+jNotification);
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
            inviteToGameRes.setMessage("Your competitor is offline now.");
        } else {
            inviteToGameRes.setMessage("It seems your competitor has entered another game.");
        }
        // create json from response
        String jResponse = mapper.writeValueAsString(inviteToGameRes);
        // send the response to the client
        printStream.println(jResponse);
    }
    private void acceptToResume(String json) {
        try {
            AcceptToResumeRequest acceptToResumeReq = mapper.readValue(json, AcceptToResumeRequest.class);
            // get the competitor
            ClientHandler _competitor = clients.get(acceptToResumeReq.getPlayer().getServer_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // link the two player with each other
                clients.get(this.getId()).competitor = _competitor;
                clients.get(acceptToResumeReq.getPlayer().getServer_id()).competitor = this;
                Match match = dbConnection.getMatch(acceptToResumeReq.getMatch().getMatch_id());
                // create resume match notification
                ResumeGameNotification resumeGameNotification = new ResumeGameNotification(match,
                        dbConnection.getPositions(acceptToResumeReq.getMatch()));
                // create json
                String jNotification = mapper.writeValueAsString(resumeGameNotification);
                // send the notification to the two players to start the game
                _competitor.printStream.println(jNotification);
                printStream.println(jNotification);
                // update in game status
                clients.get(this.getId()).myFullInfoPlayer.setInGame(true);
                clients.get(_competitor.getId()).myFullInfoPlayer.setInGame(true);
                // cast the status to all
                updateStatus(clients.get(this.getId()).myFullInfoPlayer);
                updateStatus(clients.get(_competitor.getId()).myFullInfoPlayer);
            } else {
                // the competitor became offline or started a new another game
                sendOfflineOrInGame(acceptToResumeReq.getPlayer(), _competitor);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void rejectToResume(String json) {
        try {
            // get object from json
            RejectToResumeRequest rejectToResumeReq = mapper.readValue(json, RejectToResumeRequest.class);
            // get the competitor
            ClientHandler _competitor = clients.get(rejectToResumeReq.getPlayer().getServer_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // create error response
                AskToResumeResponse askToResumeRes = new AskToResumeResponse(AskToResumeResponse.STATUS_ERROR, new Player(clients.get(this.getId()).myFullInfoPlayer));
                askToResumeRes.setMessage("It seems your competitor can not resume the game at this moment.");
                // create json from response
                String jResponse = mapper.writeValueAsString(askToResumeRes);
                // send the response to the client
                _competitor.printStream.println(jResponse);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void updateBoard(String json) {
        try {
            // get the object from the json
            UpdateBoardRequest updateBoardReq = mapper.readValue(json, UpdateBoardRequest.class);            
            //create update board notification
            UpdateBoardNotification updateBoardNotification = new UpdateBoardNotification(updateBoardReq.getPosition());
            // create json
            String jNotification = mapper.writeValueAsString(updateBoardNotification);
            // send the notification to the players
            clients.get(this.getId()).competitor.printStream.println(jNotification);
            printStream.println(jNotification);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void saveMatch(String json) {
        try {
            System.out.println("I'm in ");
            SaveMatchRequest saveMatchReq = mapper.readValue(json, SaveMatchRequest.class);
            int m_id = saveMatchReq.getMatch().getMatch_id();
            System.out.println(saveMatchReq.getMatch().getMatch_id());
            if(m_id != -1){
                System.out.println("alter");
                saveMatchReq.getMatch().setMatch_id(m_id);
                dbConnection.alterMatch(saveMatchReq.getMatch(), saveMatchReq.getPositions());
            }
            else {
                System.out.println("new");
                dbConnection.saveMatch(saveMatchReq.getMatch(), saveMatchReq.getPositions());
            }
            
            
            System.out.println("Client Handler -> saveMatch: "+ this.getId());
            System.out.println("Client Handler -> saveMatch: "+ clients.get(this.getId()));
            System.out.println("Client Handler -> saveMatch: "+ clients.get(this.getId()).competitor);
            // if the game was with a player
            if (clients.get(this.getId()).competitor != null) {
                System.out.println("here 1");
                String jResponse;
                // if status of the match is paused
                if (saveMatchReq.getMatch().getStatus().equals(Match.STATUS_PAUSED)) {
                                    System.out.println("here 2");

                    PauseGameNotification pauseGameNotification = new PauseGameNotification();
                    jResponse = mapper.writeValueAsString(pauseGameNotification);
                } else {
                                    System.out.println("here 3");

                    int u_id = saveMatchReq.getMatch().getWinner();
                                    System.out.println("here 4: "+u_id);

                    //update points
                    if (dbConnection.updatePoints(u_id)) {
                        int points = playersFullInfo.get(u_id).getPoints();
                        playersFullInfo.get(u_id).setPoints(points + 10);
                        updateStatus(playersFullInfo.get(u_id));
                    }
                    // if status of the match is finished
                    FinishGameNotification finishGameNotification = new FinishGameNotification();
                    finishGameNotification.setWinner(u_id);
                    jResponse = mapper.writeValueAsString(finishGameNotification);
                }
                // if the competitor still connected
                if (clients.get(clients.get(this.getId()).competitor.getId()) != null) {
                    // notify the competitor the game status
                    clients.get(this.getId()).competitor.printStream.println(jResponse);
                    // update competitor in game status
                    clients.get(clients.get(this.getId()).competitor.getId()).myFullInfoPlayer.setInGame(false);
                    updateStatus(clients.get(clients.get(this.getId()).competitor.getId()).myFullInfoPlayer);
                    // unlink from the competitor
                    clients.get(clients.get(this.getId()).competitor.getId()).competitor = null;
                }
                // unlink from the competitor
                clients.get(this.getId()).competitor = null;
            }
            // update in game status for the player
            clients.get(this.getId()).myFullInfoPlayer.setInGame(false);
            updateStatus(clients.get(this.getId()).myFullInfoPlayer);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("Can't Save Match! (Client Handler -> saveMatch()");
        }
    }
    private void acceptInvitation(String json) {
        try {
            // get the object from the json
            AcceptInvitationRequest acceptInvitationReq = mapper.readValue(json, AcceptInvitationRequest.class);
            // get the competitor
            ClientHandler _competitor = clients.get(acceptInvitationReq.getPlayer().getServer_id());
//            System.out.println("acceptInvitationReq = "+acceptInvitationReq.getPlayer().);
            System.out.println("_competitor = "+_competitor.getName());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // link the two player with each other
                clients.get(this.getId()).competitor = _competitor;
                clients.get(acceptInvitationReq.getPlayer().getServer_id()).competitor = this;

                // create new match
                Match match = createMatch(acceptInvitationReq.getPlayer(), clients.get(this.getId()).myFullInfoPlayer);
                // create start match notification
                StartGameNotification startGameNotification = new StartGameNotification(match);
                // create json
                String jNotification = mapper.writeValueAsString(startGameNotification);
                // send the notification to the two players to start the game
                _competitor.printStream.println(jNotification);
                printStream.println(jNotification);
                // update in game status
                clients.get(this.getId()).myFullInfoPlayer.setInGame(true);
                clients.get(_competitor.getId()).myFullInfoPlayer.setInGame(true);
                // cast the status to all
                updateStatus(clients.get(this.getId()).myFullInfoPlayer);
                updateStatus(clients.get(_competitor.getId()).myFullInfoPlayer);
            } else {
                System.out.println("offline");
                // the competitor became offline or started a new another game
                sendOfflineOrInGame(acceptInvitationReq.getPlayer(), _competitor);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private Match createMatch(Player p1, Player p2) {
        Match match = new Match();
        match.setPlayer1_id(p1.getDb_Player_id());
        match.setPlayer2_id(p2.getDb_Player_id());
        
//        String dateTimePre = new Timestamp(System.currentTimeMillis()).toString();
//        String dateTime = dateTimePre.substring(0, dateTimePre.length()-2);
//        match.setMatch_date(dateTime);
        match.setMatch_date(new Timestamp(System.currentTimeMillis()));
//        match.setMatch_date("2022-03-14 17-05-26");

        System.out.println("Client Handler > create match: "+match.getMatch_date());
        
        char[] choices = new char[]{Match.CHOICE_X, Match.CHOICE_O};
        match.setPlayer1_choice(String.valueOf(choices[new Random().nextInt(2)]));
        match.setPlayer2_choice(String.valueOf(choices[match.getPlayer1_choice().equals(String.valueOf(Match.CHOICE_X)) ? 1 : 0]));
        return match;
    }
    private void rejectInvitation(String json) {
        try {
            // get the object from the json
            RejectInvitationRequest rejectInvitationReq = mapper.readValue(json, RejectInvitationRequest.class);
            // get the competitor
            ClientHandler _competitor = clients.get(rejectInvitationReq.getPlayer().getServer_id());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                // create error response
                InviteToGameResponse inviteToGameRes = new InviteToGameResponse(InviteToGameResponse.STATUS_ERROR, new Player(clients.get(this.getId()).myFullInfoPlayer));
                inviteToGameRes.setMessage("It seems your competitor " + clients.get(this.getId()).myFullInfoPlayer.getName() + " can not play with you at this moment.");
                // create json from response
                String jResponse = mapper.writeValueAsString(inviteToGameRes);
                // send the response to the client
                _competitor.printStream.println(jResponse);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void backFromOffline(String json) {
        try {
            BackFromOfflineRequest backFromOfflineReq = mapper.readValue(json, BackFromOfflineRequest.class);
            playersFullInfo.get(backFromOfflineReq.getPlayer().getDb_Player_id()).setServer_id(this.getId());
            playersFullInfo.get(backFromOfflineReq.getPlayer().getDb_Player_id()).setStatus(Player.ONLINE);
            clients.get(this.getId()).myFullInfoPlayer = playersFullInfo.get(backFromOfflineReq.getPlayer().getDb_Player_id());
            updateStatus(clients.get(this.getId()).myFullInfoPlayer);
            LoginResponse loginRes = new LoginResponse(
                    LoginResponse.STATUS_OK,
                    clients.get(this.getId()).myFullInfoPlayer,
                    playersFullInfo
            );
            String jResponse = mapper.writeValueAsString(loginRes);
            printStream.println(jResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    public void getMatchHistory(String json) {
        try {
            //get the requested client u_id
            int u_id = clients.get(this.getId()).myFullInfoPlayer.getDb_Player_id();
            //create response
            GetMatchHistoryResponse getMatchHistoryRes = new GetMatchHistoryResponse();
            //get matches from database
            List<MatchTable> userMatches = dbConnection.getMatchHistory(u_id);
            //if there are matches, send them back to the client
            if (userMatches != null) {
                getMatchHistoryRes.setStatus(GetMatchHistoryResponse.STATUS_OK);
                getMatchHistoryRes.setMatches(userMatches);
                //convert the response to json String
                String jResponse = mapper.writeValueAsString(getMatchHistoryRes);
                printStream.println(jResponse);
            } else {
                getMatchHistoryRes.setStatus(GetMatchHistoryResponse.STATUS_ERROR);
                getMatchHistoryRes.setMessage("No Matches So Far!");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    private void askToResume(String json) {
        try {
            // get object from json
            AskToResumeRequest askToResumeReq = mapper.readValue(json, AskToResumeRequest.class);
            // get competitor
            ClientHandler _competitor = clients.get(askToResumeReq.getPlayer().getServer_id());
            System.out.println("Client Handler > Ask to resume: "+_competitor.getId());
            // check if the competitor is still online and not in a game
            if (_competitor != null && !_competitor.myFullInfoPlayer.isInGame()) {
                            System.out.println("Client Handler > Ask to resume: started");

                // create the notification
                AskToResumeNotification askToResumeNotification = new AskToResumeNotification(
                        new Player(clients.get(this.getId()).myFullInfoPlayer), askToResumeReq.getMatch());
                // create json from the notification
                String jNotification = mapper.writeValueAsString(askToResumeNotification);
                System.out.println("I'm going");
                // send the game invitation to the competitor
                _competitor.printStream.println(jNotification);
                System.out.println("I went there");
            } else {
                            System.out.println("Client Handler > Ask to resume: offline ");

                // the competitor became offline or started a new another game
                sendOfflineOrInGame(askToResumeReq.getPlayer(), _competitor);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void askToPause(String json) {
        try {
            //create notification
            AskToPauseNotification askToPauseNotification = new AskToPauseNotification();
            //convert notification object to json string
            String jNotification = mapper.writeValueAsString(askToPauseNotification);
            //send the notification to the competitor
            clients.get(this.getId()).competitor.printStream.println(jNotification);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
    private void rejectToPause(String json) {
        try {
            Response response = new Response(Response.STATUS_ERROR, Response.RESPONSE_ASK_TO_PAUSE);
            String jResponse = mapper.writeValueAsString(response);
            clients.get(this.getId()).competitor.printStream.println(jResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
    public void getPausedMatch(String json){
        try {
            GetPausedMatchRequest getPausedMatchReq = mapper.readValue(json, GetPausedMatchRequest.class);
            Match match = dbConnection.getMatch(getPausedMatchReq.getM_id());
            List<Position> positions = dbConnection.getPositions(match);
            if(positions!=null){
                GetPausedMatchResponse getPausedMatchRes = new GetPausedMatchResponse(positions, match);
                String jResponse = mapper.writeValueAsString(getPausedMatchRes);
                printStream.println(jResponse);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } {

        }
    }
    private void sendMessage(String json) {
        try {
            // get the object from the json
            SendMessageRequest sendMessageReq = mapper.readValue(json, SendMessageRequest.class);
            // create notification for message
            MessageNotification messageNotification = new MessageNotification(sendMessageReq.getMessage());
            // convert notification to json
            String jNotification = mapper.writeValueAsString(messageNotification);
            // get the competitor socket then send him the message
            clients.get(this.getId()).competitor.printStream.println(jNotification);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    interface IAction {
        void handleAction(String json);
    }
}
