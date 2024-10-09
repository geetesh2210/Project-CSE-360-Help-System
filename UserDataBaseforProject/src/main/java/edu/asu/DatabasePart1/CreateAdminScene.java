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


public class CreateAdminScene {

	 private Scene scene;
	    private SceneController controller;
	    private String adminUsername;
	    private String adminPassword;
	    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

	    public CreateAdminScene(SceneController controller) {
	        this.controller = controller;

	        // Create Pane
	        Pane pane = new Pane();

	        Label title = new Label("Create Admin Account");
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

	        PasswordField passCheck = new PasswordField();
	        passCheck.setPromptText("Re-enter Password");
	        passCheck.setLayoutX(250);
	        passCheck.setLayoutY(200);

	        Button createButton = new Button("Create");
	        createButton.setLayoutX(300);
	        createButton.setLayoutY(250);
	        createButton.setOnAction(e -> {
	            // Get username and password input
	            adminUsername = username.getText();
	            adminPassword = password.getText();

	            // Check if the password and re-entered password match
	            if (!password.getText().equals(passCheck.getText())) {
	                System.out.println("Passwords do not match!");
	                return;
	            }

	            System.out.println("Username: " + adminUsername);
	            System.out.println("Password: " + adminPassword);

	            // Database operations and scene switching
	            try {
	                databaseHelper.connectToDatabase();
	                if (!databaseHelper.doesUserExist(adminUsername)) {
	                    databaseHelper.register(adminUsername, adminPassword, "admin");
	                    databaseHelper.exportTableToFile();
	                }
	            } catch (SQLException | IOException e1) {
	                e1.printStackTrace();
	            } finally {
	                System.out.println("Database operation complete.");
	                databaseHelper.closeConnection();
	            }

	            // Switch to the Finish Setup Account scene after successful registration
	            controller.switchToFinishProfileScene();
	        });

	        // Add components to the pane
	        pane.getChildren().addAll(title, username, password, passCheck, createButton);

	        // Create the scene with the pane
	        this.scene = new Scene(pane, 700, 500);
	    }

	    // Method to return the created scene
	    public Scene getScene() {
	        return this.scene;
	    }
}