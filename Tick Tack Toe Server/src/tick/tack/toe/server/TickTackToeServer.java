package tick.tack.toe.server;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import tick.tack.toe.server.controllers.*;
import tick.tack.toe.server.controllers.client.ClientListener;

/**
 *
 * @author wwwmo
 */
public class TickTackToeServer extends Application {
    public static MainViewController controller;
    private static double xOffset = 0;
    private static double yOffset = 0;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("views/MainView.fxml"));

        
        Scene scene = new Scene(root);
        primaryStage.setTitle("Server");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //on drag
        root.setOnMousePressed((MouseEvent event) -> {
             xOffset = event.getSceneX();
             yOffset = event.getSceneY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        
        MainViewController.initClientListener();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
