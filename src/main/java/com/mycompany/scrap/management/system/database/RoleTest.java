package com.mycompany.scrap.management.system.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleTest {

    public static void main(String[] args) {

        String sql = "SELECT * FROM roles";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Read the column information from the roles table
            var metadata = resultSet.getMetaData();
            int columnCount = metadata.getColumnCount();

            System.out.println("----- Roles Table Data -----");

            // Display column names
            System.out.print("Columns: ");

            for (int i = 1; i <= columnCount; i++) {

                System.out.print(metadata.getColumnName(i));

                if (i < columnCount) {
                    System.out.print(" | ");
                }
            }

            System.out.println();
            System.out.println("----------------------------");

            // Display table records
            boolean hasRecords = false;

            while (resultSet.next()) {

                hasRecords = true;

                for (int i = 1; i <= columnCount; i++) {

                    System.out.print(resultSet.getString(i));

                    if (i < columnCount) {
                        System.out.print(" | ");
                    }
                }

                System.out.println();
            }

            if (!hasRecords) {
                System.out.println("No roles found in the database.");
            }

            System.out.println("----------------------------");
            System.out.println("Role verification completed!");

        } catch (SQLException e) {

            System.out.println("Error reading roles from database.");
            e.printStackTrace();

        }
    }
}