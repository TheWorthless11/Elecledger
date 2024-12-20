package com.example.electricity_billing_system;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateInfo {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_schemalab1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234mahhia";

    // FXML components
    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField stateField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private Button updateButton;

    // Variable to hold meter number (for user identification)
    private String meterNo;

    // Method to initialize and set the meter number from the DashboardController2
    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
        // You can use this meterNo to pre-populate fields if needed, or for the update operation.
    }

    @FXML
    public void initialize() {
        // Optional: Any initialization logic, such as populating fields from the database
    }

    @FXML
    private void handleUpdateButton() {
        // Get the values from the text fields
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String city = cityField.getText().trim();
        String state = stateField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();

        // Validate that none of the fields are empty
        if (name.isEmpty() || address.isEmpty() || city.isEmpty() || state.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert("Error", "All fields must be filled out!");
            return;
        }

        // Try to update the information in the database
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE customer SET name = ?, address = ?, city = ?, state = ?, email = ?, phone = ? WHERE meter_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, city);
            preparedStatement.setString(4, state);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, phone);
            preparedStatement.setString(7, meterNo); // Use the meter number for identifying the user

            // Execute the update query
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "Information updated successfully!");
            } else {
                showAlert("Error", "No user found with the given meter number!");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error updating information: " + e.getMessage());
        }
    }

    // Helper method to show an alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

