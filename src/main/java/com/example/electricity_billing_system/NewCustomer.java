package com.example.electricity_billing_system;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

public class NewCustomer {

    @FXML
    private TextField tfMeterNo;
    @FXML
    private TextField tfname;
    @FXML
    private TextField tfaddress;
    @FXML
    private TextField tfcity;
    @FXML
    private TextField tfstate;
    @FXML
    private TextField tfemail;
    @FXML
    private TextField tfphone;
    @FXML
    private Button submitButton;
    @FXML
    private Button cancelButton;

    private Stage popupStage;

    // Method to set the popup stage
    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }





    // Method to generate a unique meter number (e.g., UUID)
    private String generateMeterNumber() {
        return "MTR-" + UUID.randomUUID().toString().substring(0, 8); // e.g., MTR-12345678
    }

    // Initialize method to run when the form is loaded
    @FXML
    public void initialize() {
        // Set the generated meter number in the TextField
        tfMeterNo.setText(generateMeterNumber());
    }

    // Method to handle the Submit button action
    @FXML
    private void handleSubmitButton() {
        String meterNo = tfMeterNo.getText();
        String name = tfname.getText();
        String address = tfaddress.getText();
        String city = tfcity.getText();
        String state = tfstate.getText();
        String email = tfemail.getText();
        String phone = tfphone.getText();

        // Validate input fields
        if (name.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert("Validation Error", "Please fill in all fields.");
        } else {
            // Insert data into the database
            insertCustomerData(meterNo, name, address, city, state, email, phone);

            // Show success message
            showAlert("Submission Successful", "Customer details have been submitted successfully.");

            // Optionally clear the form or close the window
            clearForm();  // Optional: To clear the form after submission or close the window
        }
    }

    // Method to insert customer data into the database
    private void insertCustomerData(String meterNo, String name, String address, String city, String state, String email, String phone) {
        // Database connection and insertion code
        String sql = "INSERT INTO customer (meter_no, name, address, city, state, email, phone) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DatabaseConnection.getDbUrl(), DatabaseConnection.getDbUser(), DatabaseConnection.getDbPassword());
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, meterNo);  // Use the generated meter number
            stmt.setString(2, name);
            stmt.setString(3, address);
            stmt.setString(4, city);
            stmt.setString(5, state);
            stmt.setString(6, email);
            stmt.setString(7, phone);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer details inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to show an alert
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to clear the form fields
    private void clearForm() {
        tfMeterNo.clear();
        tfname.clear();
        tfaddress.clear();
        tfcity.clear();
        tfstate.clear();
        tfemail.clear();
        tfphone.clear();
    }

    // Method to close the popup when Cancel button is pressed
    @FXML
    private void handleCancelButton() {
        if (popupStage != null) {
            popupStage.close(); // Close the popup stage
        }
    }
}
