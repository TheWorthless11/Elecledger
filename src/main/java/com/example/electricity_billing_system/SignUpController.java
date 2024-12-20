package com.example.electricity_billing_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpController {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_schemalab1"; // Replace with your DB details
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "1234mahhia"; // Replace with your MySQL password

    @FXML
    private TextField meterNo; // Meter Number field

    @FXML
    private TextField name; // Name field

    @FXML
    private TextField username;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private Label statusLabel;

    public void initialize() {
        // Populate the role choice box with Admin and User options
        roleChoiceBox.getItems().clear();
        roleChoiceBox.getItems().addAll("Admin", "User");
        roleChoiceBox.setValue("User"); // Set default role to User
        handleRoleChange(); // Set initial state based on default role

        // Add listener to handle role change dynamically
        roleChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> handleRoleChange());
    }

    /**
     * This method disables/enables the Name and Meter Number fields based on the selected role.
     */
    private void handleRoleChange() {
        String selectedRole = roleChoiceBox.getValue();

        if ("User".equals(selectedRole)) {
            name.setDisable(true); // Disable name field for User
            meterNo.setDisable(false); // Enable meter number field for User
            name.clear(); // Clear the name field when switching to User
        } else if ("Admin".equals(selectedRole)) {
            name.setDisable(false); // Enable name field for Admin
            meterNo.setDisable(true); // Disable meter number field for Admin
            meterNo.clear(); // Clear the meter number field when switching to Admin
        }
    }

    /**
     * Handles the sign-up process by saving the new user's information.
     */
    @FXML
    public void handleSignUp() {
        String inputMeterNo = meterNo.getText(); // Get the meter number
        String inputName = name.getText(); // Get the name
        String inputUsername = username.getText();
        String inputPassword = passwordField.getText();
        String selectedRole = roleChoiceBox.getValue(); // Get the selected role (Admin or User)

        // Validate required fields based on the role
        if ("User".equals(selectedRole) && inputMeterNo.isEmpty()) {
            statusLabel.setText("Meter Number is required for User!");
            return;
        }
        if ("Admin".equals(selectedRole) && inputName.isEmpty()) {
            statusLabel.setText("Name is required for Admin!");
            return;
        }
        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            statusLabel.setText("Username and Password are required!");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // Check if username already exists for both Admin and User
            String checkQuery = "SELECT COUNT(*) FROM login WHERE username = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, inputUsername);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                statusLabel.setText("Error: Username already exists!");
                return;
            }

            // For User, also check if meter number already exists
            if ("User".equals(selectedRole)) {
                String meterCheckQuery = "SELECT COUNT(*) FROM login WHERE meter_no = ?";
                PreparedStatement meterCheckStmt = connection.prepareStatement(meterCheckQuery);
                meterCheckStmt.setString(1, inputMeterNo);
                ResultSet meterRs = meterCheckStmt.executeQuery();

                if (meterRs.next() && meterRs.getInt(1) > 0) {
                    statusLabel.setText("Error: Meter Number already exists!");
                    return;
                }
            }

            // Database insertion logic
            String query = "INSERT INTO login (meter_no, username, name, password, user) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Set values based on role
            if ("User".equals(selectedRole)) {
                preparedStatement.setString(1, inputMeterNo); // Meter number for user
                preparedStatement.setString(3, ""); // Empty name for user
            } else {
                preparedStatement.setString(1, ""); // Empty meter number for admin
                preparedStatement.setString(3, inputName); // Name for admin
            }

            preparedStatement.setString(2, inputUsername); // Username
            preparedStatement.setString(4, inputPassword); // Password (consider hashing)
            preparedStatement.setString(5, selectedRole); // Save the role (Admin/User)

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                statusLabel.setText("Sign-up successful! You can now log in.");
            } else {
                statusLabel.setText("Sign-up failed. Try again.");
            }
        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }


    @FXML
    private void handleBack() {
        try {
            // Load the Login page (login.fxml)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginlayout.fxml"));
            Parent root = loader.load();

            // Get the current stage (window) and set the scene to the Login page
            Stage stage = (Stage) username.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");

            // Show the Login page
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
