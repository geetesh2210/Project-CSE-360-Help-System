package edu.asu.DatabasePart1;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class LoginScene {

    private Scene scene;
    private SceneController controller;

    public LoginScene(SceneController controller) {
        this.controller = controller;

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
            // Perform login logic...

            // Switch to Home or Admin Home Scene based on user role
            controller.switchToHomeScene(); // or controller.switchToAdminHomeScene();
        });

        pane.getChildren().addAll(title, username, password, loginButton);
        this.scene = new Scene(pane, 700, 500);
    }

    public Scene getScene() {
        return this.scene;
    }
}