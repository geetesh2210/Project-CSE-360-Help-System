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
    private String adminPassword;
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    public FinishProfileScene(SceneController controller) {
        this.controller = controller;

        // Start with the admin creation pane
        Pane createAdminPane = getCreateAdminAccountPane();
        this.scene = new Scene(createAdminPane, 700, 500);
    }

    // Create Admin Account Pane
    private Pane getCreateAdminAccountPane() {
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

            // Database operations for admin creation
            try {
                databaseHelper.connectToDatabase();
                if (!databaseHelper.doesUserExist(adminUsername)) {
                    databaseHelper.register(adminUsername, adminPassword, "admin");
                    databaseHelper.exportTableToFile();
                }
            } catch (SQLException | IOException e1) {
                e1.printStackTrace();
            } finally {
                System.out.println("Admin creation successful.");
                databaseHelper.closeConnection();
            }

            // Switch to Finish Profile Pane
            switchToFinishProfilePane();
        });

        pane.getChildren().addAll(title, username, password, passCheck, createButton);
        return pane;
    }

    // Finish Profile Pane
    private void switchToFinishProfilePane() {
        Pane pane = new Pane();

        Label title = new Label("Finish Setting Up Account");
        title.setFont(Font.font("Arial", 25));
        title.setLayoutY(40);
        title.setLayoutX(150);

        Label usernameLabel = new Label("Username: " + adminUsername);
        usernameLabel.setLayoutX(250);
        usernameLabel.setLayoutY(100);

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

        pane.getChildren().addAll(title, usernameLabel, emailT, firstNameT, middleNameT, lastNameT, preferredNameT, finishButton);
        this.scene.setRoot(pane);  // Set the new pane as the root of the existing scene
    }

    // Method to return the created scene
    public Scene getScene() {
        return this.scene;
    }
}