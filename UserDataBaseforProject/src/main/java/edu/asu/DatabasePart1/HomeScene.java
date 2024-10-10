package edu.asu.DatabasePart1;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class HomeScene {

    private Scene scene;
    private SceneController controller;

    public HomeScene(SceneController controller) {
        this.controller = controller;

        Pane pane = new Pane();

        Button logOutButton = new Button("Log Out");
        logOutButton.setLayoutX(50);
        logOutButton.setLayoutY(450);
        logOutButton.setOnAction(e -> {
            // Switch to login scene
            controller.switchToLoginScene();
        });

        pane.getChildren().add(logOutButton);
        this.scene = new Scene(pane, 700, 500);
    }

    public Scene getScene() {
        return this.scene;
    }
}
