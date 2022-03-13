/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client.controllers.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.json.JSONObject;
import tick.tack.toe.client.TickTackToeClient;
import tick.tack.toe.client.notifications.AskToResumeNotification;
import tick.tack.toe.client.notifications.FinishGameNotification;
import tick.tack.toe.client.notifications.GameInvitationNotification;
import tick.tack.toe.client.notifications.MessageNotification;
import tick.tack.toe.client.notifications.Notification;
import tick.tack.toe.client.notifications.ResumeGameNotification;
import tick.tack.toe.client.notifications.StartGameNotification;
import tick.tack.toe.client.notifications.UpdateBoardNotification;
import tick.tack.toe.client.notifications.UpdateStatusNotification;
import tick.tack.toe.client.responses.AskToResumeResponse;
import tick.tack.toe.client.responses.GetMatchHistoryResponse;
import tick.tack.toe.client.responses.GetPausedMatchResponse;
import tick.tack.toe.client.responses.InviteToGameResponse;
import tick.tack.toe.client.responses.LoginResponse;
import tick.tack.toe.client.responses.Response;

/**
 *
 * @author wwwmo
 */
public class ServerListener extends Thread {

    private static final int PORT = 5050;
    private static final String HOST = "localhost";
    private static PrintStream printStream;
    private Socket socket;
    private BufferedReader bufferedReader;
    private Map<String, IType> types;
    private boolean running, first;

    public ServerListener() {
        running = true;
        first = true;
        initTypes();
        initConnection();
    }

    private void initConnection() {
        try {
            socket = new Socket(HOST, PORT);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            printStream = new PrintStream(socket.getOutputStream());
            System.out.println("connected");
//            backFromOffline();
            first = false;
        } catch (Exception ex) {
            System.out.println("Failed to Connect Due to the Server is Down");
            if (first) {
                first = false;
                goOffline();
            }
            
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ignored) { }
            
            initConnection();
        }
    }
    
    private void initTypes() {
        types = new HashMap<>();
        types.put(Response.RESPONSE_LOGIN, this::Login);
        types.put(Response.RESPONSE_INVITE_TO_GAME, this::inviteToGameResponse);
        types.put(Response.RESPONSE_SIGN_UP, this::signUpRes);
        types.put(Response.RESPONSE_GET_MATCH_HISTORY, this::getMatchHistory);
        types.put(Response.RESPONSE_ASK_TO_RESUME, this::rejectToResumeGame);
        types.put(Response.RESPONSE_GET_PAUSED_MATCH, this::getPausedMatch);
        types.put(Response.RESPONSE_ASK_TO_PAUSE, this::askToPauseResponse);
//
        types.put(Notification.NOTIFICATION_UPDATE_STATUS, this::updateStatus);
        types.put(Notification.NOTIFICATION_GAME_INVITATION, this::gameInvitation);
        types.put(Notification.NOTIFICATION_START_GAME, this::startGame);
        types.put(Notification.NOTIFICATION_ASK_TO_PAUSE, this::askToPauseNotification);
        types.put(Notification.NOTIFICATION_MESSAGE, this::sendMessageRes);
        types.put(Notification.NOTIFICATION_ASK_TO_RESUME, this::askToResume);
        types.put(Notification.NOTIFICATION_RESUME_GAME, this::resumeGame);
        types.put(Notification.NOTIFICATION_FINISH_GAME, this::finishGameNotification);
        types.put(Notification.NOTIFICATION_PAUSE_GAME, this::pauseGameNotification);
        types.put(Notification.NOTIFICATION_COMPETITOR_CONNECTION_ISSUE, this::competitorConnectionIssueNotification);
        types.put(Notification.NOTIFICATION_UPDATE_BOARD, this::updateBoardNotification);
    }
    
    private void goOffline() {
        if (!first) {
            Platform.runLater(() -> {
                TickTackToeClient.showAlert("Disconnected",
                        "Unfortunately disconnected from the server please check your internet connection",
                        Alert.AlertType.ERROR);

                TickTackToeClient.homeController.offline(true);
                if (TickTackToeClient.openedScene != TickTackToeClient.scenes.vsComputerS && TickTackToeClient.openedScene != TickTackToeClient.scenes.homeS) {
                    TickTackToeClient.openHomeView();
                }
            });
        }
    }
    
    private void getMatchHistory(String json) {
        try {
            GetMatchHistoryResponse getMatchHistoryRes=TickTackToeClient.mapper.readValue(json,GetMatchHistoryResponse.class);
            TickTackToeClient.matchController.fillMatchesTable(getMatchHistoryRes.getMatches());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void askToResume(String json) {
        try {
            AskToResumeNotification askToResumeNotification = TickTackToeClient.mapper.readValue(json, AskToResumeNotification.class);
            Platform.runLater(() -> TickTackToeClient.homeController.addResumeReq(askToResumeNotification));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void askToPauseResponse(String json) {
        Platform.runLater(() -> TickTackToeClient.gameController.handleAskToPauseResponse());
    }
    
    private void askToPauseNotification(String json) {
        Platform.runLater(() -> TickTackToeClient.gameController.notifyAskToPause());
    }
    
    private void resumeGame(String json) {
        try {
            ResumeGameNotification resumeGameNotification = TickTackToeClient.mapper.readValue(json, ResumeGameNotification.class);
            Platform.runLater(() -> TickTackToeClient.gameController.confirmResume(resumeGameNotification));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    public void rejectToResumeGame(String json){
        try{
            AskToResumeResponse askToResumeRes = TickTackToeClient.mapper.readValue(json, AskToResumeResponse.class);
            if(askToResumeRes.getStatus().equals(Response.STATUS_ERROR)){
                Platform.runLater(() -> TickTackToeClient.homeController.declineResume(askToResumeRes));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void updateBoardNotification(String json) {
        try {
            UpdateBoardNotification updateBoardNotification = TickTackToeClient.mapper.readValue(json, UpdateBoardNotification.class);
            Platform.runLater(() -> TickTackToeClient.gameController.handleUpdateBoard(updateBoardNotification.getPosition()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void pauseGameNotification(String json) {
        Platform.runLater(() -> TickTackToeClient.gameController.handlePauseGame());
    }
    
    private void finishGameNotification(String json) {
        try {
            FinishGameNotification finishGameNotification = TickTackToeClient.mapper.readValue(json, FinishGameNotification.class);
            Platform.runLater(() -> TickTackToeClient.gameController.handleFinishGame(finishGameNotification));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void inviteToGameResponse(String json) {
        try {
            InviteToGameResponse inviteToGameRes = TickTackToeClient.mapper.readValue(json, InviteToGameResponse.class);
            Platform.runLater(() -> TickTackToeClient.homeController.inviteToGameResponse(inviteToGameRes));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void startGame(String json) {
        try {
            StartGameNotification startGameNotification = TickTackToeClient.mapper.readValue(json, StartGameNotification.class);
            Platform.runLater(() -> TickTackToeClient.homeController.startGame(startGameNotification.getMatch()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void getPausedMatch(String json){
        try {
            GetPausedMatchResponse getPausedMatchRes = TickTackToeClient.mapper.readValue(json, GetPausedMatchResponse.class);
            Platform.runLater(()->TickTackToeClient.gameController.viewMatchHistory(getPausedMatchRes));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void competitorConnectionIssueNotification(String json) {
        Platform.runLater(() -> TickTackToeClient.gameController.competitorConnectionIssue());
    }
    
    private void sendMessageRes(String json) {
        try {
            System.out.println("1");
            MessageNotification messageNotification = TickTackToeClient.mapper.readValue(json, MessageNotification.class);
            Platform.runLater(() -> TickTackToeClient.gameController.handleMessageNotification(messageNotification));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
    
    
    /****************************************************/
    public static void sendRequest(String json) {
        try {
            System.out.println(json);
            printStream.println(json);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
    /***************************************************/
    /**
     * Handle Response
     */
    private void Login(String json) {
        try {
            LoginResponse loginRes = TickTackToeClient.mapper.readValue(json, LoginResponse.class);
            System.out.println("hello: "+ loginRes.getStatus());
            Platform.runLater(() -> TickTackToeClient.loginController.handleResponse(loginRes));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void signUpRes(String json) {
        try {
            Response signUpRes = TickTackToeClient.mapper.readValue(json, Response.class);
            Platform.runLater(() -> TickTackToeClient.registerController.handleResponse(signUpRes));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
    private void updateStatus(String json) {
        try {
            UpdateStatusNotification updateStatusNotification = TickTackToeClient.mapper.readValue(json, UpdateStatusNotification.class);
            Platform.runLater(() -> TickTackToeClient.homeController.updateStatus(updateStatusNotification.getPlayerFullInfo()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
    
    private void gameInvitation(String json) {
        try {
            GameInvitationNotification gameInvitationNotification = TickTackToeClient.mapper.readValue(json, GameInvitationNotification.class);
            Platform.runLater(() -> TickTackToeClient.homeController.notifyGameInvitation(gameInvitationNotification.getPlayer()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    /*****************************************************/
    @Override
    public void run() {
        while (running) {
            try {
                String sMessage = bufferedReader.readLine();
                System.out.println(sMessage);
                JSONObject json = new JSONObject(sMessage);
                String serverType = (String) json.get("type");
                if (types.get(serverType) != null) {
                    types.get(serverType).handleAction(sMessage);
                    System.out.println("Client Notificaion: -> "+sMessage.toString());
                }
            } catch (Exception e) {
                if (running) {
                    goOffline();
                    initConnection();
                }
            }
        }
    }
    
    @Override
    public void interrupt() throws NullPointerException{
        super.interrupt();
        try {
            System.out.println("h");
            socket.close();
                        System.out.println("1h");

            first=false;
                        System.out.println("2h");

            running=false;
                        System.out.println("3h");

            goOffline();
        } catch (IOException e) {
            System.out.println("what");
        } 
    }

    
    
    interface IType {

        void handleAction(String json);
    }
}