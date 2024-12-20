package com.example.electricity_billing_system;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DashboardController2 {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_schemalab1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234mahhia";

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button logoutButton;

    @FXML
    private MenuButton infoMenuButton;

    @FXML
    private MenuButton userMenuButton;

    @FXML
    private Button deleteAccountButton;

    private String meterNo; // Store meter number
@FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick()
    {
        String url = "https://api.myjson.online/v1/records/05e89c47-4343-4619-a180-354d342feb34";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
            HttpResponse<String> response= client.send(request, HttpResponse.BodyHandlers.ofString());
            jsonParse(response.body());
        }catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void jsonParse(String response)
    {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
        JsonArray jsonArray=jsonObject.get("data").getAsJsonArray();
        StringBuilder stringBuilder=new StringBuilder();
        for(int i=0;i<jsonArray.size();i++)
        {
            JsonObject  text = jsonArray.get(i).getAsJsonObject();
            String msg = text.get("message").getAsString();
            stringBuilder.append(msg);
        }
        welcomeText.setText(stringBuilder.toString());
    }

    public void setMeterNo(String meterNo) {
        this.meterNo = meterNo; // Set the meter number when passing from LoginController
    }

    @FXML
    public void initialize() {
        // Initialize the MenuButton for Information section
        MenuItem updateInfoItem = new MenuItem("Update Info");
        updateInfoItem.setOnAction(event -> handleInfoMenuAction("Update Info"));

        MenuItem viewInfoItem = new MenuItem("View Info");
        viewInfoItem.setOnAction(event -> handleInfoMenuAction("View Info"));

        infoMenuButton.getItems().addAll(updateInfoItem, viewInfoItem);

        // Initialize the MenuButton for User section
        MenuItem payBillItem = new MenuItem("Pay Bill");
        payBillItem.setOnAction(event -> handleUserMenuAction("Pay Bill"));

        MenuItem billDetailsItem = new MenuItem("Bill Details");
        billDetailsItem.setOnAction(event -> handleUserMenuAction("Bill Details"));

        userMenuButton.getItems().addAll(payBillItem, billDetailsItem);
    }

    @FXML
    public void handleLogout() {
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
    }

    @FXML
    private void handleInfoMenuAction(String selectedOption) {
        System.out.println("Info Menu selected: " + selectedOption);
        // Handle the selected option for Information (View Info or Update Info)
        // You can load different scenes or perform actions based on the selection
        if (selectedOption.equals("Update Info")) {
            // Open the Update Info window
            openNewWindow("UpdateInfo.fxml", "Update Info Window");
        } else if (selectedOption.equals("View Info")) {
            // Open the View Info window
            openNewWindow("ViewInfo.fxml", "View Info Window");
        }
    }

    @FXML
    private void handleUserMenuAction(String selectedOption) {
        System.out.println("User Menu selected: " + selectedOption);
        // Handle the selected option for User (Pay Bill or Bill Details)
        // You can load different scenes or perform actions based on the selection
        if (selectedOption.equals("Pay Bill")) {
            // Open the Pay Bill window
            openNewWindow("PayBill.fxml", "Pay Bill Window");
        } else if (selectedOption.equals("Bill Details")) {
            // Open the Bill Details window
            openNewWindow("BillDetails.fxml", "Bill Details Window");
        }
    }

    @FXML
    public void handleDeleteAccount() {
        String username = usernameField.getText().trim();  // Get the username from the text field
        String password = passwordField.getText().trim();  // Get the password from the password field

        // Check if the username or password is empty
        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username and Password must not be empty!");
            return;
        }

        // Try to delete the user from the login table based on username and password
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            // SQL query to delete the user
            String query = "DELETE FROM login WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            int rowsDeleted = preparedStatement.executeUpdate(); // Execute the delete query

            // Show a message depending on whether the deletion was successful
            if (rowsDeleted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid username or password. No user deleted.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error: " + e.getMessage());
        }
    }

    // Helper method to show alert messages
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openNewWindow(String fxmlFile, String windowTitle) {
        try {
            // Load the specified FXML file and create a new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Pass the meter_no to the ViewInfoController
            if (fxmlFile.equals("viewInfo.fxml")) {
                ViewInfo controller = loader.getController();
                controller.setMeterNo(this.meterNo); // Pass meter_no to ViewInfoController
            }

            Scene scene = new Scene(root);

            // Create a new stage (window)
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setTitle(windowTitle);
            newStage.show();
        } catch (IOException e) {
            System.err.println("Error opening new window: " + e.getMessage());
        }
    }
}

