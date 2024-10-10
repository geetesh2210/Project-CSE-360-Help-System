package edu.asu.DatabasePart1;

import java.sql.SQLException;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class RoleSelectionScene {

    private Scene scene;
    private SceneController controller;
    private String user;

    public RoleSelectionScene(SceneController controller, String username){
        this.controller = controller;
       // this.username = username;
        Pane pane = new Pane();
        Label label = new Label("Select Role");
        label.setFont(Font.font("Arial", 25));
        label.setLayoutX(250);
        label.setLayoutY(100);
        String[] roles =  new String[10];
        
        try 
        {
        	DatabaseHelper dbHelper = new DatabaseHelper();
        	dbHelper.connectToDatabase();
        	String roleS = dbHelper.getCurrentRoles(username);
        	roles = roleS.split(" ");
        }
        catch(SQLException e1)
        {
        	e1.printStackTrace();
        }
        int index = 0;
        for(String role : roles)
        {
        	Button instructorButton = new Button(role);
            instructorButton.setLayoutX(200*(index + 1));
            instructorButton.setLayoutY(200);
            instructorButton.setOnAction(e -> {
                // Switch to instructor home screen
            	if(role.equals("ADMIN"))
            	{
            		controller.switchToAdminHomeScene();
            	}
            	else
            	{
            		controller.switchToHomeScene();
            	}
            });
            pane.getChildren().add(instructorButton);
            index++;
        }
        


        pane.getChildren().addAll(label);
        this.scene = new Scene(pane, 700, 500);
    }

    public Scene getScene() {
        return this.scene;
    }

}