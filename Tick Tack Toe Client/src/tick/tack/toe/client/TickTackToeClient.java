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
import java.util.Optional;
import java.net.URL;
import tick.tack.toe.client.controllers.server.ServerListener;

import tick.tack.toe.client.models.*;

/**
 *
 * @author wwwmo
 */
public class TickTackToeClient extends Application {
    
     private static Stage mainStage;
    private static Scene sceneRegister, sceneHome, sceneGame, sceneLogin, sceneMatch, sceneGameVsComputer;

    private URL url;
    private String css;
//    public static RegisterController registerController;
//    public static HomeController homeController;
//    public static GameController gameController;
//    public static LoginController loginController;
//    public static MatchController matchController;
//    public static GameVsComputerController gameVsComputerController;
    private static ServerListener serverListener;
//    public static final ObjectMapper mapper = new ObjectMapper();
    private static TrayIcon trayIcon;
    private SystemTray tray;

    public enum scenes {registerS, loginS, homeS, matchS, vsPlayerS, vsComputerS}

    public static scenes openedScene;

    @Override
    public void init() throws Exception {
        super.init();
        serverListener = new ServerListener();
        initViews();
        initTray();
        serverListener.setDaemon(true);
        serverListener.start();
    }
    
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/login.fxml"));

        Scene loginScene = new Scene(root);
        primaryStage.setTitle("Login");
        primaryStage.setResizable(false);
        primaryStage.setScene(loginScene);
        
        File iconfile = new File("views/imgs/xocolored-0١.png");
        Image icon = new Image(iconfile.toURI().toString());
        primaryStage.getIcons().add(icon);
        
        mainStage = primaryStage;   
        primaryStage.show();
    }

    private void initViews() {
        try {
            // Register view
            sceneRegister = new Scene(FXMLLoader.load(getClass().getResource("views/Register.fxml")));

            // Home view
            sceneRegister = new Scene(FXMLLoader.load(getClass().getResource("views/Home.fxml")));

            // Game View
            sceneRegister = new Scene(FXMLLoader.load(getClass().getResource("views/Game.fxml")));

            // GameVsComputer View
            sceneRegister = new Scene(FXMLLoader.load(getClass().getResource("views/GameVsComputer.fxml")));

            // Match View
            sceneRegister = new Scene(FXMLLoader.load(getClass().getResource("views/Match.fxml")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initTray() {
        //Obtain only one instance of the SystemTray object
        if (SystemTray.isSupported()) {
            tray = SystemTray.getSystemTray();

            //If the icon is a file
            java.awt.Image image = Toolkit.getDefaultToolkit().getImage("views/imgs/xocolored-0١.png");

            trayIcon = new TrayIcon(image, "Tic Tac Toe Game");

            //Let the system resize the image if needed
            trayIcon.setImageAutoSize(true);
            //Set tooltip text for the tray icon
            trayIcon.setToolTip("Tic Tac Toe Game");
            trayIcon.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Platform.runLater(() -> {
                        mainStage.requestFocus();
                        mainStage.toFront();
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
    public static void showSystemNotification(String title, String message, TrayIcon.MessageType messageType) {
        if (SystemTray.isSupported()) {
            trayIcon.displayMessage(title, message, messageType);
        } else {
            System.err.println("System tray not supported!");
        }
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
