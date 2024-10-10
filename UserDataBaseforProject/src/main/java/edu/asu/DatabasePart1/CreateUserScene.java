package edu.asu.DatabasePart1;

import java.io.IOException;
import java.sql.SQLException;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class CreateUserScene {

    private Scene scene;
    private SceneController controller;
    private String userUsername;
    private String userPassword;
    private String invCode;
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();
    
    public CreateUserScene(SceneController controller) {
        this.controller = controller;

        Pane pane = new Pane();

        Label title = new Label("Create User Account");
        title.setFont(Font.font("Arial", 25));
        title.setLayoutY(40);
        title.setLayoutX(200);

        TextField username = new TextField();
        username.setPromptText("Create Username");
        username.setLayoutX(250);
        username.setLayoutY(100);

        PasswordField password = new PasswordField();
        password.setPromptText("Create Password");
        password.setLayoutX(250);
        password.setLayoutY(150);
        
        TextField invitationCode = new TextField();
        invitationCode.setPromptText("Invitation Code");
        invitationCode.setLayoutX(250);
        invitationCode.setLayoutY(200);
        
        Button backToLoginButton = new Button("Back to Login");
        backToLoginButton.setLayoutX(350);
        backToLoginButton.setLayoutY(250);
        backToLoginButton.setOnAction(e -> {
            // Switch to the Login scene
            controller.switchToLoginScene();
        });

        Button createButton = new Button("Create");
        createButton.setLayoutX(300);
        createButton.setLayoutY(250);
        createButton.setOnAction(e -> {
            // Perform user account creation logic...
        	 userUsername = username.getText();
             userPassword = password.getText();
             invCode = invitationCode.getText();
            // Switch to Home Scene
             try {
	                if (userUsername.isEmpty() || userPassword.isEmpty() || invCode.isEmpty()) {
	                    showAlert("Username, password, or invitation code cannot be empty.");
	                } 
	                else {
	                    databaseHelper.connectToDatabase();
	                    if (!databaseHelper.doesUserExist(userUsername)) {
	                    	//determine role
	                    	String role = determineRole(invCode);
	                        if (role == null) {
	                            showAlert("Invalid invitation code. Please try again.");
	                        } else {
	                        databaseHelper.register(userUsername, userPassword, role);
	                        databaseHelper.exportTableToFile();
	                        // Switch to the Finish Setup Account scene only after successful registration
	                        controller.switchToFinishProfileScene();
	                    } 
	                    }else {
	                        showAlert("Username already exists.");
	                    }
	                }
	            } catch (SQLException | IOException ex) {
	                ex.printStackTrace();
	                showAlert("An error occurred during registration.");
	            } finally {
	                System.out.println("Database operation complete.");
	                databaseHelper.closeConnection();
	            }
	            
	        });

        pane.getChildren().addAll(title, username, password, invitationCode, backToLoginButton, createButton);
        this.scene = new Scene(pane, 700, 500);
    }

    public Scene getScene() {
        return this.scene;
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private String determineRole(String invCode) {
        // Map invitation codes to roles
        switch (invCode.toLowerCase()) {
            case "student":
                return "student";
            case "instructor":
                return "instructor";
            case "both":
                return "both";
            default:
                return null; // Invalid invitation code
        }
    }
}