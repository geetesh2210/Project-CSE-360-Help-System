package edu.asu.DatabasePart1;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class LoginScene {

    private Scene scene;
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();
    public LoginScene(SceneController controller) {
        Pane pane = new Pane();

        Label title = new Label("Log In");
        title.setFont(Font.font("Arial", 25));
        title.setLayoutY(40);
        title.setLayoutX(300);

        TextField username = new TextField();
        username.setPromptText("Username");
        username.setLayoutX(250);
        username.setLayoutY(100);

        PasswordField password = new PasswordField();
        password.setPromptText("Password");
        password.setLayoutX(250);
        password.setLayoutY(150);

        Button loginButton = new Button("Log In");
        loginButton.setLayoutX(300);
        loginButton.setLayoutY(200);
        loginButton.setOnAction(e -> {
        	if (username.getText().isEmpty() || password.getText().isEmpty()) {
                showAlert("Username and password cannot be empty.");
            } else {
                try {
                    // Validate username and password
                    databaseHelper.connectToDatabase();
                    String user = username.getText();
                    String pass = password.getText();
                    
                   
                    
                    if (databaseHelper.doesUserExist(user) && databaseHelper.login(user, pass)) {
                        // Switch to Home or Admin Home Scene based on user role
                    	 
						 String roles = databaseHelper.getCurrentRoles(user); 
						 //boolean OTP = databaseHelper.isPasswordReset(user);
						 
					
						 databaseHelper.closeConnection();
						 
						 int index = roles.indexOf(" ");
						 if(index != -1) {
							 
							 controller.switchToRoleSelectionScene(user);
							 
							 
						 }
						 else if(roles.equals("ADMIN")) {
							 
							 controller.switchToAdminHomeScene();
						 }
						 else { controller.switchToHomeScene();
                    	
                    	
                    } } else {
                        showAlert("Invalid username or password.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("An error occurred during login.");
                } finally {
                    
                }
            }
        });

       

        Button createUserButton = new Button("Create New User");
        createUserButton.setLayoutX(270);
        createUserButton.setLayoutY(250);
        createUserButton.setOnAction(e -> {
            // Switch to Create User Scene
            controller.switchToCreateUserScene();
        });

        pane.getChildren().addAll(title, username, password, createUserButton, loginButton);
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
}