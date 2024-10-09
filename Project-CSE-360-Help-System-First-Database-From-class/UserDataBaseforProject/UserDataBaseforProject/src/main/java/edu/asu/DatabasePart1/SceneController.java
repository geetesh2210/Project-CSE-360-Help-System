package edu.asu.DatabasePart1;

import javafx.stage.Stage;

public class SceneController {

    private Stage primaryStage;

    public SceneController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Switch to "Create Admin" scene
    public void switchToCreateAdminScene() {
        CreateAdminScene createAdminScene = new CreateAdminScene(this);
        primaryStage.setScene(createAdminScene.getScene());
    }

    // Switch to "Finish Profile" scene
    public void switchToFinishProfileScene() {
        FinishProfileScene finishProfileScene = new FinishProfileScene(this);
        primaryStage.setScene(finishProfileScene.getScene());
    }

    // Switch to "Login" scene
    public void switchToLoginScene() {
        LoginScene loginScene = new LoginScene(this);
        primaryStage.setScene(loginScene.getScene());
    }

    // Switch to "Create User" scene
    public void switchToCreateUserScene() {
        CreateUserScene createUserScene = new CreateUserScene(this);
        primaryStage.setScene(createUserScene.getScene());
    }

    // Switch to "Home" scene
    public void switchToHomeScene() {
        HomeScene homeScene = new HomeScene(this);
        primaryStage.setScene(homeScene.getScene());
    }

    // Switch to "Admin Home" scene
    public void switchToAdminHomeScene() {
        AdminHomeScene adminHomeScene = new AdminHomeScene(this);
        primaryStage.setScene(adminHomeScene.getScene());
    }
}