package com.example.electricity_billing_system;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewInfo{

    // Labels to display customer information

    @FXML
    private TextField meterNoTextField;

    @FXML
    private Label nameLabel;

    @FXML
    private TextField nameField;

    @FXML
    private Label meterNoLabel;

    @FXML
    private TextField meterNoField;

    @FXML
    private Label addressLabel;

    @FXML
    private TextField addressField;

    @FXML
    private Label cityLabel;

    @FXML
    private TextField cityField;

    @FXML
    private Label stateLabel;

    @FXML
    private TextField stateField;

    @FXML
    private Label emailLabel;

    @FXML
    private TextField emailField;

    @FXML
    private Label phoneLabel;

    @FXML
    private TextField phoneField;

    @FXML
    private Button closeButton;

    @FXML
    private Label toast;

    private String meterNo; // Store the meter number to fetch customer data

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo;
        fetchCustomerInfo();
    }

    @FXML

    private void fetchCustomerInfo() {

        String meterNo = meterNoTextField.getText();
        // Database connection details
        String DB_URL = "jdbc:mysql://localhost:3306/new_schemalab1";
        String DB_USER = "root";
        String DB_PASSWORD = "1234mahhia";  // Replace with your database password

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM customer WHERE meter_no = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, meterNo);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Populate the labels with customer information from the database
                nameField.setText(resultSet.getString("name"));
                meterNoField.setText(resultSet.getString("meter_no"));
                addressField.setText(resultSet.getString("address"));
                cityField.setText(resultSet.getString("city"));
                stateField.setText(resultSet.getString("state"));
                emailField.setText(resultSet.getString("email"));
                phoneField.setText(resultSet.getString("phone"));
            } else {
                // If no data found, display a message
                toast.setText("No data found for the customer.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            toast.setText("Error loading customer data.");
        }
    }
    // Close button action
    @FXML
    private void handleClose() {
        // Get the current stage (window)
        Stage stage = (Stage) closeButton.getScene().getWindow(); // Use one of the UI elements in the scene to get the window (e.g., nameLabel)

        // Close the window
        stage.close();
    }
}
