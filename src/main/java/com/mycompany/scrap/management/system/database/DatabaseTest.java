package com.mycompany.scrap.management.system.database;

import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {

        try {

            // Initialize database tables
            DatabaseInitializer.initializeDatabase();

            // Insert default roles
            DatabaseSeeder.seedDefaultRoles();

            System.out.println(
                    "Database setup test completed successfully!"
            );

            System.out.println(
                    "Database path: "
                    + DatabaseConnection.getDatabasePath()
            );

        } catch (SQLException e) {

            System.out.println(
                    "Database setup failed!"
            );

            e.printStackTrace();

        }
    }
}