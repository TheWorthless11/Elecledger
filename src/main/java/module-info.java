module com.example.electricity_billing_system {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;
    requires java.net.http;
    requires com.google.gson;
    requires java.sql;


    opens com.example.electricity_billing_system to javafx.fxml;
    exports com.example.electricity_billing_system;
}