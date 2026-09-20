package com.mycompany.scrap.management.system.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSeeder {

    public static void seedDefaultRoles() {

        String sql = """
                INSERT OR IGNORE INTO roles (role_name, description)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Insert Admin role
            statement.setString(1, "Admin");
            statement.setString(2, "Full access to the system");
            statement.executeUpdate();

            // Insert Manager role
            statement.setString(1, "Manager");
            statement.setString(2, "Manage purchasing, inventory, and sales");
            statement.executeUpdate();

            // Insert Staff role
            statement.setString(1, "Staff");
            statement.setString(2, "Handle daily operational tasks");
            statement.executeUpdate();

            System.out.println("Default roles inserted successfully!");

        } catch (SQLException e) {

            System.out.println("Error inserting default roles.");
            e.printStackTrace();

        }
    }
}