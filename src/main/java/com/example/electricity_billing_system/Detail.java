package com.example.electricity_billing_system;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Detail {

    @FXML
    private TableView<CustomerRecord> customerTable;

    @FXML
    private TableColumn<CustomerRecord, String> colMeterNo;

    @FXML
    private TableColumn<CustomerRecord, String> colName;

    @FXML
    private TableColumn<CustomerRecord, String> colAddress;

    @FXML
    private TableColumn<CustomerRecord, String> colCity;

    @FXML
    private TableColumn<CustomerRecord, String> colState;

    @FXML
    private TableColumn<CustomerRecord, String> colEmail;

    @FXML
    private TableColumn<CustomerRecord, String> colPhone;

    private ObservableList<CustomerRecord> customerData;

    @FXML
    public void initialize() {
        customerData = FXCollections.observableArrayList();

        // Set up the table columns
        colMeterNo.setCellValueFactory(new PropertyValueFactory<>("meterNo"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        colState.setCellValueFactory(new PropertyValueFactory<>("state"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Load data from the database
        loadCustomerData();
    }

    private void loadCustomerData() {
        String query = "SELECT * FROM customer";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                customerData.add(new CustomerRecord(
                        rs.getString("meter_no"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("city"),
                        rs.getString("state"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }

            // Add data to the TableView
            customerTable.setItems(customerData);

        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
        }
    }
}
