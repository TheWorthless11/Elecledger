package com.example.electricity_billing_system;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_schemalab1"; // Replace with your DB details
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "1234mahhia"; // Replace with your MySQL password

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label wronglogin;

    @FXML
    private Button cancelbutton;

    @FXML
    private Button signupButton;

    @FXML
    private ChoiceBox<String> mychoicebox;

    private String[] person ={"Admin","User"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the ChoiceBox with options for "Admin" and "User"
        mychoicebox.getItems().addAll("Admin", "User");

        // Set default value (optional)
        mychoicebox.setValue("Admin");
    }

    /**
     * Handles the login process by validating the user's credentials.
     */
    @FXML
    public void setwronglogin() {
        String inputUsername = username.getText().trim();
        String inputPassword = password.getText().trim();
        String selectedRole = mychoicebox.getValue();  // Get the selected value from the ChoiceBox

        // Clear previous error messages
        wronglogin.setText("");

        // Check for empty fields
        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            wronglogin.setText("Please fill in both fields!");
            return;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT password, user, meter_no FROM login WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, inputUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password");
                String storedRole = resultSet.getString("user"); // Role column
                String storedMeterNo = resultSet.getString("meter_no"); // For user validation

                // Check if the password matches
                if (storedPassword.equals(inputPassword)) {
                    // Check if the role selected matches the role stored in the database
                    if (!storedRole.equals(selectedRole)) {
                        wronglogin.setText("Invalid role selected!");
                        return;
                    }

                    // For user, check meter_no is not empty
                    if ("User".equals(selectedRole) && storedMeterNo == null) {
                        wronglogin.setText("Invalid User credentials!");
                        return;
                    }

                    // Navigate to the correct dashboard based on the selected role
                    Stage stage = (Stage) username.getScene().getWindow();
                    FXMLLoader loader;

                    // Load the correct dashboard based on user role
                    if ("Admin".equals(selectedRole)) {
                        loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
                    } else if ("User".equals(selectedRole)) {

                        loader = new FXMLLoader(getClass().getResource("dashboard2.fxml"));

                    } else {
                        // If for some reason, neither "Admin" nor "User" is selected
                        wronglogin.setText("Invalid role selected!");
                        return;
                    }

                    // If it's a "User", pass the meter_no to the dashboard
                    if ("User".equals(selectedRole)) {
                        // Load the FXMLLoader and get the controller after loading the FXML
                        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("dashboard2.fxml"));
                        Parent root = loader1.load();  // Make sure to load the FXML file here

                        // Now that the controller is initialized, set the meter number
                        DashboardController2 controller = loader1.getController();
                        controller.setMeterNo(storedMeterNo);

                        // Proceed to set the scene and show the stage
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle(selectedRole + " Dashboard");
                        stage.show();
                    }




                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.setTitle(selectedRole + " Dashboard"); // Set the title based on the role
                    stage.show();
                } else {
                    wronglogin.setText("Incorrect password!");
                }
            } else {
                wronglogin.setText("Username not found!");
            }
        } catch (SQLException e) {
            wronglogin.setText("Database error: " + e.getMessage());
        } catch (Exception e) {
            wronglogin.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Handles the action for the Cancel button, closing the application.
     */
    @FXML
    public void setCancelbutton() {
        Stage stage = (Stage) cancelbutton.getScene().getWindow();
        stage.close();
    }

    /**
     * Handles the action for the Sign-Up button, opening the Sign-Up window.
     */
    @FXML
    public void handleSignUp() {
        try {
            // Load the Sign-Up FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup.fxml"));
            Parent root = loader.load();

            // Create a new Stage (window) for the Sign-Up form
            Stage signUpStage = new Stage();
            signUpStage.setTitle("Sign-Up");
            signUpStage.setScene(new Scene(root));

            // Optional: Make the current window disabled while the Sign-Up window is open
            signUpStage.initModality(Modality.WINDOW_MODAL);
            signUpStage.initOwner(cancelbutton.getScene().getWindow());

            // Show the Sign-Up window
            signUpStage.showAndWait();
        } catch (Exception e) {
            System.err.println("Error loading Sign-Up page: " + e.getMessage());
        }
    }
}
