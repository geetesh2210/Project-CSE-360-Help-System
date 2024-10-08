package edu.asu.DatabasePart1;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class windows{

    private Stage primaryStage;
    private static String adminUsername;
    private static String adminPassword;
    private String userName;
    private String userPassword;
    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    public windows(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public Pane getCreateAdminAccountPane() throws SQLException {
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
	        adminUsername = username.getText();
	        adminPassword = password.getText();
	        System.out.println(adminUsername);
	        System.out.println(adminPassword);
	        
	        primaryStage.setScene(new Scene(getFinishSetupAccountPane(), Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
	        try 
	        {
				databaseHelper.connectToDatabase();
				if(!databaseHelper.doesUserExist(adminUsername))
				{
					databaseHelper.register(adminUsername, adminPassword, "admin");
					databaseHelper.exportTableToFile();
				}
			} 
	        catch (SQLException e1) 
	        {
				e1.printStackTrace();
			} 
	        catch (IOException e2)
	        {
				e2.printStackTrace();
	        }
	        finally
	        {
	        	System.out.println("Success");
	        	databaseHelper.closeConnection();
	        }   
	    });
	        
	    pane.getChildren().addAll(title, username, password, passCheck, createButton);
        return pane;
    }
   
    public Pane getFinishSetupAccountPane() {
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
            primaryStage.setScene(new Scene(getLoginPane(), Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
            String email, firstName, middleName, lastName, preName;
            email = emailT.getText();
            firstName = firstNameT.getText();
            middleName = middleNameT.getText();
            lastName = lastNameT.getText();
            preName = preferredNameT.getText();
            try 
	        {
				databaseHelper.connectToDatabase();
				databaseHelper.updateInfo("admin", email, firstName, middleName, lastName, preName);
				databaseHelper.exportTableToFile();
			} 
	        catch (SQLException e1) 
	        {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
	        catch (IOException e2)
	        {
				e2.printStackTrace();
	        }
	        finally
	        {
	        	System.out.println("Success");
	        	databaseHelper.closeConnection();
	        }               
        });

        pane.getChildren().addAll(title, usernameLabel, emailT, firstNameT, middleNameT, lastNameT, preferredNameT, finishButton);
        return pane;
    }

    public Pane getLoginPane() {
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
            primaryStage.setScene(new Scene(getHomePane(), Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
            
        });

        Button createAccountButton = new Button("Create Account");
        createAccountButton.setLayoutX(290);
        createAccountButton.setLayoutY(250);
        createAccountButton.setOnAction(e -> {
            primaryStage.setScene(new Scene(getCreateAccountPane(), Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
        });

        pane.getChildren().addAll(title, username, password, loginButton, createAccountButton);
        return pane;
    }

    public Pane getCreateAccountPane() {
        Pane pane = new Pane();

        Label title = new Label("Create Account");
        title.setFont(Font.font("Arial", 25));
        title.setLayoutY(40);
        title.setLayoutX(250);

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
            primaryStage.setScene(new Scene(getFinishSetupAccountPane(), Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
        });

        pane.getChildren().addAll(title, username, password, passCheck, createButton);
        return pane;
    }

    public Pane getHomePane() {
        Pane pane = new Pane();

        Button logOutButton = new Button("Log Out");
        logOutButton.setLayoutX(50);
        logOutButton.setLayoutY(450);
        logOutButton.setOnAction(e -> {
            primaryStage.setScene(new Scene(getLoginPane(), Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT));
        });

        pane.getChildren().add(logOutButton);
        return pane;
    }
}