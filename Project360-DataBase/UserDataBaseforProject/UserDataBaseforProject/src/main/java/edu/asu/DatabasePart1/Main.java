package edu.asu.DatabasePart1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public final static double WINDOW_WIDTH = 700;
    public final static double WINDOW_HEIGHT = 500;
    public windows theGUI;

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Help Desk"); // Label the stage (a window)
        
        theGUI = new windows(primaryStage); // Pass the Stage to MainInterface
        
        Scene theScene = new Scene(theGUI.getCreateAdminAccountPane(), WINDOW_WIDTH, WINDOW_HEIGHT); // Create the initial scene
        
        primaryStage.setScene(theScene); // Set the scene on the stage
        
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
