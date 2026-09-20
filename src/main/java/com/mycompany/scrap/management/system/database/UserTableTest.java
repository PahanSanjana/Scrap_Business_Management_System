package com.mycompany.scrap.management.system.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserTableTest {

    public static void main(String[] args) {

        String sql = "SELECT * FROM users";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Get information about the users table columns
            var metadata = resultSet.getMetaData();

            int columnCount = metadata.getColumnCount();

            System.out.println("----- Users Table Structure -----");

            // Display column names
            System.out.print("Columns: ");

            for (int i = 1; i <= columnCount; i++) {

                System.out.print(metadata.getColumnName(i));

                if (i < columnCount) {
                    System.out.print(" | ");
                }
            }

            System.out.println();
            System.out.println("---------------------------------");

            // Display existing users
            boolean hasRecords = false;

            while (resultSet.next()) {

                hasRecords = true;

                for (int i = 1; i <= columnCount; i++) {

                    String value = resultSet.getString(i);

                    // Avoid displaying password values
                    String columnName =
                            metadata.getColumnName(i).toLowerCase();

                    if (columnName.contains("password")
                            || columnName.contains("hash")
                            || columnName.contains("salt")) {

                        value = "[HIDDEN]";
                    }

                    System.out.print(value);

                    if (i < columnCount) {
                        System.out.print(" | ");
                    }
                }

                System.out.println();
            }

            if (!hasRecords) {
                System.out.println("No users found in the database.");
            }

            System.out.println("---------------------------------");
            System.out.println("Users table verification completed!");

        } catch (SQLException e) {

            System.out.println("Error reading users table.");
            e.printStackTrace();

        }
    }
}