package com.example.electricity_billing_system;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class DashboardController {

    @FXML
    private MenuButton masterButton;

    @FXML
    private Button logoutButton;

    @FXML
    private VBox optionsVBox;  // VBox to hold the 4 option buttons

    @FXML
    private Button newCustomerButton;
    @FXML
    private Button customerDetailsButton;
    @FXML
    private Button depositDetailsButton;
    @FXML
    private Button calculateBillButton;

    @FXML
    private void handleMasterButton(ActionEvent event) {
        System.out.println("Master button clicked");

        // Toggle the visibility of the options
        if (optionsVBox.isVisible()) {
            optionsVBox.setVisible(false);
        } else {
            optionsVBox.setVisible(true);
        }
    }



    @FXML
    private void handleLogoutButton(ActionEvent event) {
        System.out.println("Log Out button clicked");

        // Get the current stage (window)
        Stage stage = (Stage) logoutButton.getScene().getWindow();

        try {
            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginlayout.fxml"));
            Parent root = loader.load();

            // Set the scene to the login screen and show it
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");  // Change the title to "Login"
            stage.show();

        } catch (IOException e) {
            System.err.println("Error loading login page: " + e.getMessage());
        }

        // Optionally, close the current window if needed
        // stage.close();
    }

    // Methods for each of the four option buttons
    @FXML
    private void handleNewCustomer(ActionEvent event) {
        System.out.println("New Customer option clicked");
        showPopup("newCustomer.fxml", "Customer");
    }

    @FXML
    private void handleCustomerDetails(ActionEvent event) {
        System.out.println("Customer Details option clicked");
        showPopup("customerDetails.fxml", "Detail");
    }

    @FXML
    private void handleDepositDetails(ActionEvent event) {
        System.out.println("Deposit Details option clicked");
        showPopup("depositDetails.fxml", "Deposit");
    }

    @FXML
    private void handleCalculateBill(ActionEvent event) {
        System.out.println("Calculate Bill option clicked");
        showPopup("calculateBill.fxml", "Calculate");
    }

    private void showPopup(String fxmlFileName, String controllerClass) {
        try {
            // Load the popup FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            Parent root = loader.load();

            // Create a new stage (popup window)
            Stage popupStage = new Stage();
            popupStage.setTitle("Popup Window");

            // Set the scene for the popup window
            Scene scene = new Scene(root);
            popupStage.setScene(scene);

            // Get the controller for the new scene
            Object controller = loader.getController();

            // If the controller class is Customer, pass the data or initialize it if necessary
            if (controllerClass.equals("Customer") && controller instanceof NewCustomer) {
                NewCustomer customerController = (NewCustomer) controller;
                customerController.setPopupStage(popupStage);  // Example of passing the popup stage
            }

//            // Add a Cancel button in the popup to close it
//            Button cancelButton = (Button) root.lookup("#cancelButton");
//            cancelButton.setOnAction(e -> popupStage.close());  // Close the popup when clicked

            // Show the popup
            popupStage.show();

        } catch (IOException e) {
            System.err.println("Error loading popup: " + e.getMessage());
        }
    }

}
