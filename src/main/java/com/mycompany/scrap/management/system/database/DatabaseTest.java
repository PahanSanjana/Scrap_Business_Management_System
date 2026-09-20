
package com.mycompany.scrap.management.system.database;

import java.sql.SQLException;

public class DatabaseTest {

    public static void main(String[] args) {

        try {

            DatabaseInitializer.initializeDatabase();

            System.out.println(
                    "Database setup test completed successfully!"
            );

            System.out.println(
                    "Database path: "
                    + DatabaseConnection.getDatabasePath()
            );

        } catch (SQLException exception) {

            System.out.println(
                    "Database setup test failed!"
            );

            exception.printStackTrace();
        }
    }
}