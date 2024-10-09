package edu.asu.DatabasePart1;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class CreateUserScene {

    private Scene scene;
    private SceneController controller;

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

        Button createButton = new Button("Create");
        createButton.setLayoutX(300);
        createButton.setLayoutY(250);
        createButton.setOnAction(e -> {
            // Perform user account creation logic...

            // Switch to Home Scene
            controller.switchToHomeScene();
        });

        pane.getChildren().addAll(title, username, password, createButton);
        this.scene = new Scene(pane, 700, 500);
    }

    public Scene getScene() {
        return this.scene;
    }
}