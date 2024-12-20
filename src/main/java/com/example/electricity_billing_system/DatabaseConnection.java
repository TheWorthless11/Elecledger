package com.example.electricity_billing_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database credentials
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_schemalab1";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234mahhia";

    // Method to establish connection
    public static Connection getConnection() throws SQLException {
        try {
            // Register the MySQL JDBC driver (not required in recent versions of Java)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish and return the connection to the database
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            // Handle exceptions and return null if connection fails
            System.err.println("Error establishing database connection: " + e.getMessage());
            throw new SQLException("Unable to connect to the database.");
        }
    }

    // Getter methods for DB details
    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }
}
