package edu.asu.DatabasePart1;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.sql.SQLException;

public class AdminHomeScene {
    private Scene scene;
    private SceneController controller;
    private final DatabaseHelper databaseHelper = new DatabaseHelper(); // Assuming you want to instantiate it here

    public AdminHomeScene(SceneController controller) {
        this.controller = controller;

        Pane pane = new Pane();
        
        // Logout Button
        Button logOutButton = new Button("Log Out");
        logOutButton.setLayoutX(50);
        logOutButton.setLayoutY(450);
        logOutButton.setOnAction(e -> controller.switchToLoginScene());

        // Reset User Account Button
        Button resetUserButton = new Button("Reset User Account");
        resetUserButton.setLayoutX(50);
        resetUserButton.setLayoutY(50);
        TextField resetUsernameField = new TextField();
        resetUsernameField.setPromptText("Enter username");
        resetUsernameField.setLayoutX(200);
        resetUsernameField.setLayoutY(50);
        resetUserButton.setOnAction(e -> {
            String username = resetUsernameField.getText();
            try {
            	databaseHelper.connectToDatabase();
                String code = databaseHelper.resetUserAccount(username);
                showAlert(code.isEmpty() ? "User not found." : "Reset code sent: " + code);
                databaseHelper.closeConnection();
            } catch (SQLException ex) {
                showAlert("An error occurred while resetting the user account.");
                ex.printStackTrace();
            }
        });

        // Add User Role Button
        Button addUserRoleButton = new Button("Add User Role");
        addUserRoleButton.setLayoutX(50);
        addUserRoleButton.setLayoutY(100);
        TextField addRoleUsernameField = new TextField();
        addRoleUsernameField.setPromptText("Enter username");
        addRoleUsernameField.setLayoutX(200);
        addRoleUsernameField.setLayoutY(100);
        TextField addRoleField = new TextField();
        addRoleField.setPromptText("Enter role to add");
        addRoleField.setLayoutX(400);
        addRoleField.setLayoutY(100);
        addUserRoleButton.setOnAction(e -> {
        	
            String username = addRoleUsernameField.getText();
            String roleToAdd = addRoleField.getText();
            try {
            	databaseHelper.connectToDatabase();
                boolean success = databaseHelper.addUserRole(username, roleToAdd);
                showAlert(success ? "Role added successfully." : "User not found or role already exists.");
                databaseHelper.closeConnection();
            } catch (SQLException ex) {
                showAlert("An error occurred while adding the user role.");
                ex.printStackTrace();
            }
        });

        // Remove User Role Button
        Button removeUserRoleButton = new Button("Remove User Role");
        removeUserRoleButton.setLayoutX(50);
        removeUserRoleButton.setLayoutY(150);
        TextField removeRoleUsernameField = new TextField();
        removeRoleUsernameField.setPromptText("Enter username");
        removeRoleUsernameField.setLayoutX(200);
        removeRoleUsernameField.setLayoutY(150);
        TextField removeRoleField = new TextField();
        removeRoleField.setPromptText("Enter role to remove");
        removeRoleField.setLayoutX(400);
        removeRoleField.setLayoutY(150);
        removeUserRoleButton.setOnAction(e -> {
            
            try {
            	databaseHelper.connectToDatabase();
            	String username = removeRoleUsernameField.getText();
                String roleToRemove = removeRoleField.getText();
            	
                boolean success = databaseHelper.removeUserRole(username, roleToRemove);
                showAlert(success ? "Role removed successfully." : "User not found or role does not exist.");
                databaseHelper.closeConnection();
            } catch (SQLException ex) {
                showAlert("An error occurred while removing the user role.");
                ex.printStackTrace();
            }
        });

        // Delete User Account Button
        Button deleteUserButton = new Button("Delete User Account");
        deleteUserButton.setLayoutX(50);
        deleteUserButton.setLayoutY(200);
        TextField deleteUsernameField = new TextField();
        deleteUsernameField.setPromptText("Enter username");
        deleteUsernameField.setLayoutX(200);
        deleteUsernameField.setLayoutY(200);
        deleteUserButton.setOnAction(e -> {
            String username = deleteUsernameField.getText();
            try {
            	databaseHelper.connectToDatabase();
                boolean success = databaseHelper.deleteUserAccount(username);
                showAlert(success ? "User account deleted successfully." : "User not found.");
                databaseHelper.closeConnection();
            } catch (SQLException ex) {
                showAlert("An error occurred while deleting the user account.");
                ex.printStackTrace();
            }
        });

        // Display Users Button
        Button displayUsersButton = new Button("Display All Users");
        displayUsersButton.setLayoutX(50);
        displayUsersButton.setLayoutY(250);
        displayUsersButton.setOnAction(e -> {
            try {
            	databaseHelper.connectToDatabase();
                databaseHelper.displayUsersByAdmin(); // Assuming this method handles output internally
                showAlert("Check console for user list.");
                databaseHelper.closeConnection();
            } catch (SQLException ex) {
                showAlert("An error occurred while displaying users.");
                ex.printStackTrace();
            }
        });

        // Adding buttons and fields to the pane
        pane.getChildren().addAll(logOutButton, resetUserButton, addUserRoleButton, removeUserRoleButton, deleteUserButton, displayUsersButton, 
                                   resetUsernameField, addRoleUsernameField, addRoleField, removeRoleUsernameField, removeRoleField, deleteUsernameField);
        
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
