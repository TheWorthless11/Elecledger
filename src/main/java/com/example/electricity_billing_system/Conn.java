package com.example.electricity_billing_system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Conn {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_schemalab1"; // Replace with your DB details
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "1234mahhia"; // Replace with your MySQL password

    public Connection con;
    public Statement s;

    public Conn() {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            // Create a statement for executing queries
            s = con.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

