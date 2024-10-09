package edu.asu.DatabasePart1;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import java.io.IOException;
import java.sql.SQLException;

public class FinishProfileScene {

	private Scene scene;
    private SceneController controller;
    private String adminUsername;
    
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    public FinishProfileScene(SceneController controller) {
        this.controller = controller;
        Pane pane = new Pane();  // Initialize a blank pane for the scene
        this.scene = new Scene(pane, 700, 500);// Initialize the scene with the pane
        switchToFinishProfilePane();
    }
    
   
    // Finish Profile Pane
    private void switchToFinishProfilePane() {
        Pane pane = new Pane();

       
        
        Label title = new Label("Finish Setting Up Account");
        title.setFont(Font.font("Arial", 25));
        title.setLayoutY(40);
        title.setLayoutX(150);

        

        TextField emailT = new TextField();
        emailT.setPromptText("Enter Email");
        emailT.setLayoutX(250);
        emailT.setLayoutY(150);

        TextField firstNameT = new TextField();
        firstNameT.setPromptText("First Name");
        firstNameT.setLayoutX(250);
        firstNameT.setLayoutY(200);

        TextField middleNameT = new TextField();
        middleNameT.setPromptText("Middle Name");
        middleNameT.setLayoutX(250);
        middleNameT.setLayoutY(250);

        TextField lastNameT = new TextField();
        lastNameT.setPromptText("Last Name");
        lastNameT.setLayoutX(250);
        lastNameT.setLayoutY(300);

        TextField preferredNameT = new TextField();
        preferredNameT.setPromptText("Preferred Name");
        preferredNameT.setLayoutX(250);
        preferredNameT.setLayoutY(350);

        Button finishButton = new Button("Finish");
        finishButton.setLayoutX(300);
        finishButton.setLayoutY(400);
        finishButton.setOnAction(e -> {
            String email = emailT.getText();
            String firstName = firstNameT.getText();
            String middleName = middleNameT.getText();
            String lastName = lastNameT.getText();
            String preName = preferredNameT.getText();

            // Database operations for profile update
            try {
                databaseHelper.connectToDatabase();
                databaseHelper.updateInfo(adminUsername, email, firstName, middleName, lastName, preName);
                databaseHelper.exportTableToFile();
            } catch (SQLException | IOException e1) {
                e1.printStackTrace();
            } finally {
                System.out.println("Profile update successful.");
                databaseHelper.closeConnection();
            }

            // Switch to the login scene
            controller.switchToLoginScene();
        });

        pane.getChildren().addAll(title, emailT, firstNameT, middleNameT, lastNameT, preferredNameT, finishButton);
        this.scene.setRoot(pane);  // Set the new pane as the root of the existing scene
    }

    // Method to return the created scene
    public Scene getScene() {
        return this.scene;
    }
}
