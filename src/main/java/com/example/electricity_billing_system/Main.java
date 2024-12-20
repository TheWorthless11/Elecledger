package com.example.electricity_billing_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML file for the Detail UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard2.fxml"));
            Scene scene = new Scene(loader.load());

            // Set the title and scene
            primaryStage.setTitle("Customer Details");
            primaryStage.setScene(scene);

            // Retrieve the controller to pass the stage reference
            DashboardController2 controller = loader.getController();

            // Show the stage
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
