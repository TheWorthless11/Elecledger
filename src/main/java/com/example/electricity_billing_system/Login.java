package com.example.electricity_billing_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Login extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Correct path to load FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginlayout.fxml"));
        Parent root = loader.load();

        // Create scene and set on stage
        Scene scene = new Scene(root, 650, 400);
        //setLocation(400,200);
       // primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Login Page");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
