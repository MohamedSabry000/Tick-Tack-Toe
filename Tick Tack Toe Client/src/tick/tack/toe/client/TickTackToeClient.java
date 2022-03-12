/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tick.tack.toe.client;

//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.iti.tictactoeclient.controllers.*;
//import com.iti.tictactoeclient.helpers.ServerListener;
//import com.iti.tictactoeclient.requests.UpdateInGameStatusReq;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.net.URL;
import javafx.stage.StageStyle;
import tick.tack.toe.client.controllers.*;
import tick.tack.toe.client.controllers.server.ServerListener;
import tick.tack.toe.client.requests.UpdateInGameStatusRequest;


/**
 *
 * @author wwwmo
 */
public class TickTackToeClient extends Application {
    
    private static Stage mainStage;
    private static Scene sceneRegister, sceneHome, sceneGame, sceneLogin, sceneMatch, sceneGameVsComputer, sceneTTT, sceneScore;
    public static final ObjectMapper mapper = new ObjectMapper();

    private URL url;
    private String css;
    public static RegisterViewController registerController;
    public static HomeViewController homeController;
    public static TicTacToeViewController gameController;
    public static LoginViewController loginController;
    public static MatchViewController matchController;
    public static GameVsComputerViewController gameVsComputerController;
    private static ServerListener serverListener;
    private static TrayIcon trayIcon;
    private SystemTray tray;

    public enum scenes {registerS, loginS, homeS, matchS, vsPlayerS, vsComputerS}

    public static scenes openedScene;

    @Override
    public void init() throws Exception {
        super.init();
        serverListener = new ServerListener();
        initViews();
//        initTray();
        serverListener.setDaemon(true);
        serverListener.start();
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TickTackToeClient.class.getResource("views/Login.fxml"));
        sceneLogin = new Scene(fxmlLoader.load());
        loginController = fxmlLoader.getController();
  
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(sceneLogin);
        
        File iconfile = new File("views/imgs/xocolored-0١.png");
        Image icon = new Image(iconfile.toURI().toString());
        primaryStage.getIcons().add(icon);
        
        mainStage = primaryStage;   
        primaryStage.show();
    }

    private void initViews() {
        try {
            // Register view
            FXMLLoader fxmlLoaderLogin = new FXMLLoader(TickTackToeClient.class.getResource("views/Login.fxml"));
            sceneLogin = new Scene(fxmlLoaderLogin.load());
            loginController = fxmlLoaderLogin.getController();
            
            // Register view
            FXMLLoader fxmlLoaderRegister = new FXMLLoader(TickTackToeClient.class.getResource("views/Register.fxml"));
            sceneRegister = new Scene(fxmlLoaderRegister.load());
            registerController = fxmlLoaderRegister.getController();

            // Home view
            FXMLLoader fxmlLoaderHome = new FXMLLoader(TickTackToeClient.class.getResource("views/Home.fxml"));
            sceneHome = new Scene(fxmlLoaderHome.load());
            homeController = fxmlLoaderHome.getController();

            // Game View
            sceneScore = new Scene(FXMLLoader.load(getClass().getResource("views/Score.fxml")));

            // GameVsComputer View
            FXMLLoader fxmlLoaderGameVsComputer = new FXMLLoader(TickTackToeClient.class.getResource("views/GameVsComputer.fxml"));
            sceneGameVsComputer = new Scene(fxmlLoaderGameVsComputer.load());
            gameVsComputerController = fxmlLoaderGameVsComputer.getController();

            // Match View
            FXMLLoader fxmlLoaderMatch = new FXMLLoader(TickTackToeClient.class.getResource("views/Match.fxml"));
            sceneMatch = new Scene(fxmlLoaderMatch.load());
            matchController = fxmlLoaderMatch.getController();
            
            // Match View
            FXMLLoader fxmlLoaderGame = new FXMLLoader(TickTackToeClient.class.getResource("views/Tic Tac Toe.fxml"));
            sceneTTT = new Scene(fxmlLoaderGame.load());
            gameController = fxmlLoaderGame.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    private void initTray() {
//        //Obtain only one instance of the SystemTray object
//        if (SystemTray.isSupported()) {
//            tray = SystemTray.getSystemTray();
//
//            //If the icon is a file
//            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("views/imgs/xocolored-0١.png");
//
//            trayIcon = new TrayIcon(image, "Tic Tac Toe Game");
//
//            //Let the system resize the image if needed
//            trayIcon.setImageAutoSize(true);
//            //Set tooltip text for the tray icon
//            trayIcon.setToolTip("Tic Tac Toe Game");
//            trayIcon.addMouseListener(new MouseListener() {
//                @Override
//                public void mouseClicked(MouseEvent e) {
//                    Platform.runLater(() -> {
//                        mainStage.requestFocus();
//                        mainStage.toFront();
//                    });
//                }
//
//                @Override
//                public void mousePressed(MouseEvent e) {}
//                @Override
//                public void mouseReleased(MouseEvent e) {}
//                @Override
//                public void mouseEntered(MouseEvent e) {}
//                @Override
//                public void mouseExited(MouseEvent e) {}
//            });
//            try {
//                tray.add(trayIcon);
//            } catch (AWTException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    
    public static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText("");
        alert.showAndWait();
    }
//    public static void showSystemNotification(String title, String message, TrayIcon.MessageType messageType) {
//        if (SystemTray.isSupported()) {
//            trayIcon.displayMessage(title, message, messageType);
//        } else {
//            System.err.println("System tray not supported!");
//        }
//    }
    
    public static boolean showConfirmation(String title, String message, String btn1, String btn2) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "",
                new ButtonType(btn1, ButtonBar.ButtonData.OK_DONE),
                new ButtonType(btn2, ButtonBar.ButtonData.CANCEL_CLOSE));
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText("");
        Optional<ButtonType> s = alert.showAndWait();
        final boolean[] isOk = new boolean[1];
        s.ifPresent(buttonType -> {
            isOk[0] = buttonType.getButtonData().isDefaultButton();
        });
        return isOk[0];
    }
    
    
    public static void openLoginView() {

        mainStage.hide();
        mainStage.setScene(sceneLogin);
        mainStage.setTitle("Login");
        File iconfile = new File("views/imgs/xocolored-0١");
        Image icon = new Image(iconfile.toURI().toString());
        mainStage.getIcons().add(icon);
//        loginController.showAnimation();
        openedScene = scenes.loginS;
        mainStage.show();
    }
    public static void openRegisterView() {
        mainStage.hide();
        mainStage.setScene(sceneRegister);
        mainStage.setTitle("Register");
        File iconfile = new File("views/imgs/xocolored-0١");
        Image icon = new Image(iconfile.toURI().toString());
        mainStage.getIcons().add(icon);
//        registerController.showAnimation();
        openedScene = scenes.registerS;
        mainStage.show();
    }
    public static void openHomeView() {
        mainStage.hide();
        mainStage.setScene(sceneHome);
        mainStage.setTitle("Home");
        File iconfile = new File("views/imgs/xocolored-0١");
        Image icon = new Image(iconfile.toURI().toString());
        mainStage.getIcons().add(icon);
        openedScene = scenes.homeS;
        mainStage.show();
//        homeController.showAnimation();
    }
    public static void openMatchView() {
        mainStage.hide();
        mainStage.setScene(sceneMatch);
        mainStage.setTitle("Match");
        File iconfile = new File("views/imgs/xocolored-0١");
        Image icon = new Image(iconfile.toURI().toString());
        mainStage.getIcons().add(icon);
        openedScene = scenes.matchS;
        mainStage.show();
//        matchController.showAnimation();
    }
    public static void openGameView() {
        mainStage.hide();
        mainStage.setScene(sceneGame);
        mainStage.setTitle("TicTacToe");
        File iconfile = new File("views/imgs/xocolored-0١");
        Image icon = new Image(iconfile.toURI().toString());
        mainStage.getIcons().add(icon);
        openedScene = scenes.vsPlayerS;
        mainStage.show();
//        gameController.showAnimation();
    }
    public static void openGameVsComputerView() {
        mainStage.hide();
        mainStage.setScene(sceneGameVsComputer);
        mainStage.setTitle("TicTacToe");
        File iconfile = new File("images/7.png");
        Image icon = new Image(iconfile.toURI().toString());
        mainStage.getIcons().add(icon);
//        gameVsComputerController.showAnimation();
        openedScene = scenes.vsComputerS;
        mainStage.show();
    }

    public static void showAlert(String title, String type) {

        FXMLLoader loader = new FXMLLoader(TickTackToeClient.class.getResource("views/"+type + ".fxml"));
        try {
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle(title);

            stage.setScene(scene);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            System.out.println("tick.tack.toe.client.TickTackToeClient.showAlert()");
        }
    }
    public static void sendUpdateInGameStatus(boolean isInGame) {
        if (homeController.getMyPlayerFullInfo() != null) {
            UpdateInGameStatusRequest updateInGameStatusReq = new UpdateInGameStatusRequest(isInGame);
            try {
                String jRequest = TickTackToeClient.mapper.writeValueAsString(updateInGameStatusReq);
                ServerListener.sendRequest(jRequest);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void stop() throws Exception {
        try{
            super.stop();
            tray.remove(trayIcon);
            serverListener.interrupt();
        } catch(NullPointerException e){}
        
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
