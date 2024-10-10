package edu.asu.DatabasePart1;


import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
	    private String confirmPassword;
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
	            confirmPassword = passCheck.getText();

	            try {
	                if (adminUsername.isEmpty() || adminPassword.isEmpty()) {
	                    showAlert("Username and password cannot be empty.");
	                } else if (!adminPassword.equals(confirmPassword)) {
	                    showAlert("Passwords must match.");
	                } else {
	                    databaseHelper.connectToDatabase();
	                    if (!databaseHelper.doesUserExist(adminUsername)) {
	                        databaseHelper.register(adminUsername, adminPassword, "admin");
	                        databaseHelper.exportTableToFile();
	                        System.out.println("Database operation complete.");
	    	                databaseHelper.closeConnection();
	                        // Switch to the Finish Setup Account scene only after successful registration
	                        controller.switchToFinishProfileScene();
	                    } else {
	                        showAlert("Username already exists.");
	                    }
	                }
	            } catch (SQLException | IOException ex) {
	                ex.printStackTrace();
	                showAlert("An error occurred during registration.");
	            } finally {
	                
	            }
	            
	        });
	            // Database operations and scene switching
	       

	        // Add components to the pane
	        pane.getChildren().addAll(title, username, password, passCheck, createButton);

	        // Create the scene with the pane
	        this.scene = new Scene(pane, 700, 500);
	    }

	    // Method to return the created scene
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
}
