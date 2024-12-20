package com.example.electricity_billing_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Deposit {

    @FXML
    private ComboBox<String> meterNumberDropdown;  // ComboBox for meter number

    @FXML
    private ComboBox<String> monthDropdown;

    @FXML
    private TableView<Bill> billTable;

    @FXML
    private TableColumn<Bill, String> meterNoColumn;

    @FXML
    private TableColumn<Bill, String> monthColumn;

    @FXML
    private TableColumn<Bill, String> unitsColumn;

    @FXML
    private TableColumn<Bill, String> totalBillColumn;

    @FXML
    private TableColumn<Bill, String> statusColumn;

    private ObservableList<Bill> billData = FXCollections.observableArrayList();
    private ObservableList<String> meterNumbers = FXCollections.observableArrayList();  // List for meter numbers

    @FXML
    public void initialize() {
        // Initialize the dropdown with months
        monthDropdown.getItems().addAll("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

        // Set up the table columns
        meterNoColumn.setCellValueFactory(data -> data.getValue().meterNoProperty());
        monthColumn.setCellValueFactory(data -> data.getValue().monthProperty());
        unitsColumn.setCellValueFactory(data -> data.getValue().unitsProperty());
        totalBillColumn.setCellValueFactory(data -> data.getValue().totalBillProperty());
        statusColumn.setCellValueFactory(data -> data.getValue().statusProperty());

        // Load meter numbers from the database into the ComboBox
        loadMeterNumbers();
    }

    // Load the meter numbers into the ComboBox
    private void loadMeterNumbers() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT meter_no FROM bill");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                meterNumbers.add(resultSet.getString("meter_no"));
            }

            meterNumberDropdown.setItems(meterNumbers);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error fetching meter numbers: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearchByMeter() {
        String meterNumber = meterNumberDropdown.getValue();  // Get selected meter number from ComboBox

        if (meterNumber == null || meterNumber.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select a meter number.");
            return;
        }

        fetchBills("SELECT * FROM bill WHERE meter_no = ?", meterNumber);
    }

    @FXML
    private void handleSearchByMonth(MouseEvent event) {
        String selectedMonth = monthDropdown.getValue();

        if (selectedMonth == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select a month.");
            return;
        }

        fetchBills("SELECT * FROM bill WHERE month = ?", selectedMonth);
    }

    private void fetchBills(String query, String parameter) {
        billData.clear();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, parameter);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                billData.add(new Bill(
                        resultSet.getString("meter_no"),
                        resultSet.getString("month"),
                        resultSet.getString("units"),
                        resultSet.getString("totalbill"),
                        resultSet.getString("status")
                ));
            }

            billTable.setItems(billData);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error fetching bill details: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
