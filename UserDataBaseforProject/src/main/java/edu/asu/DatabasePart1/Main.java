package edu.asu.DatabasePart1;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public final static double WINDOW_WIDTH = 700;
    public final static double WINDOW_HEIGHT = 500;
    public windows theGUI;
    

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Help Desk"); // Label the stage (a window)
        
        DatabaseHelper dbHelper = new DatabaseHelper();
        dbHelper.connectToDatabase();
        
        
        SceneController sceneController = new SceneController(primaryStage);

        boolean isDatabaseEmpty = dbHelper.isDatabaseEmpty();
        
        if (isDatabaseEmpty) {
        // Set the initial scene (e.g., the admin creation pane)
        sceneController.switchToCreateAdminScene();
        }
        else {
            // If users exist, switch to the regular user creation scene
            sceneController.switchToLoginScene();
        }
        // Show the stage with the initial scene
        primaryStage.show();
        
        
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}
