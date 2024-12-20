package com.example.electricity_billing_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Calculate {

    @FXML
    private ComboBox<String> meterComboBox;

    @FXML
    private TextField nameField;

    @FXML
    private TextField addressField;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private TextField unitsField;

    @FXML
    private TextField totalBillField;

    @FXML
    private Button submitButton;

    @FXML
    private Button cancelButton;

    private Stage popupStage;

    public void setPopupStage(Stage stage) {
        this.popupStage = stage;
    }

    @FXML
    public void initialize() {
        // Load meter numbers into ComboBox
        loadMeterNumbers();

        // Initialize months ComboBox
        monthComboBox.setItems(FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        ));

        // Set listener to load customer details when a meter number is selected
        meterComboBox.setOnAction(event -> loadCustomerDetails());
    }

    private void loadMeterNumbers() {
        String query = "SELECT meter_no FROM customer";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ObservableList<String> meterNumbers = FXCollections.observableArrayList();
            while (rs.next()) {
                meterNumbers.add(rs.getString("meter_no"));
            }
            meterComboBox.setItems(meterNumbers);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomerDetails() {
        String selectedMeter = meterComboBox.getValue();
        if (selectedMeter == null) return;

        String query = "SELECT name, address FROM customer WHERE meter_no = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, selectedMeter);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                nameField.setText(rs.getString("name"));
                addressField.setText(rs.getString("address"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCalculateBill() {
        try {
            int units = Integer.parseInt(unitsField.getText());
            if (units < 0) {
                showAlert("Error", "Units cannot be negative.", Alert.AlertType.ERROR);
                return;
            }
            double costPerUnit = getTaxDetail("cost_per_unit");
            double meterRent = getTaxDetail("meter_rent");
            double serviceCharge = getTaxDetail("service_charge");
            double serviceTax = getTaxDetail("service_tax");
            double municipalTax = getTaxDetail("municipal_tax");
            double fixedTax = getTaxDetail("fixed_tax");

            double totalBill = (units * costPerUnit) + meterRent + serviceCharge +
                    (units * costPerUnit * serviceTax / 100) +
                    municipalTax + fixedTax;

            totalBillField.setText(String.format("%.2f", totalBill));

        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid input for units. Please enter a valid number.", Alert.AlertType.ERROR);
        }
    }

    private double getTaxDetail(String taxType) {
        String query = "SELECT " + taxType + " FROM tax";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return Double.parseDouble(rs.getString(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @FXML
    private void handleSubmit() {
        String meterNo = meterComboBox.getValue();
        String month = monthComboBox.getValue();
        String units = unitsField.getText();
        String totalBill = totalBillField.getText();

        if (meterNo == null || month == null || units.isEmpty() || totalBill.isEmpty()) {
            showAlert("Error", "All fields must be filled.", Alert.AlertType.ERROR);
            return;
        }

        String query = "INSERT INTO bill (meter_no, month, units, totalbill, status) VALUES (?, ?, ?, ?, 'Pending')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, meterNo);
            stmt.setString(2, month);
            stmt.setString(3, units);
            stmt.setString(4, totalBill);

            stmt.executeUpdate();
            showAlert("Success", "Bill successfully submitted.", Alert.AlertType.INFORMATION);

            // Clear the form
            meterComboBox.setValue(null);
            nameField.clear();
            addressField.clear();
            monthComboBox.setValue(null);
            unitsField.clear();
            totalBillField.clear();

            // Close the popup stage
            if (popupStage != null) {
                popupStage.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        if (popupStage != null) {
            popupStage.close();
        } else {
            // Fallback: Close the current window
            Stage currentStage = (Stage) cancelButton.getScene().getWindow();
            currentStage.close();
        }
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
