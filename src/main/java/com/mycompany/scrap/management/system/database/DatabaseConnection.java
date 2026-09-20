
package com.mycompany.scrap.management.system.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseConnection {

    private static final String APPLICATION_FOLDER =
            "ScrapBusinessManagement";

    private static final Path DATA_DIRECTORY =
            Paths.get(
                    System.getProperty("user.home"),
                    APPLICATION_FOLDER,
                    "data"
            );

    private static final Path DATABASE_PATH =
            DATA_DIRECTORY.resolve("scrap_business.db");

    private static final String DATABASE_URL =
            "jdbc:sqlite:" + DATABASE_PATH;

    private DatabaseConnection() {
        // Prevent creating objects from this utility class.
    }

    public static Connection getConnection() throws SQLException {

        createDataDirectory();

        Connection connection =
                DriverManager.getConnection(DATABASE_URL);

        configureConnection(connection);

        return connection;
    }

    private static void createDataDirectory()
            throws SQLException {

        try {
            Files.createDirectories(DATA_DIRECTORY);
        } catch (IOException exception) {

            throw new SQLException(
                    "Unable to create database directory: "
                    + DATA_DIRECTORY,
                    exception
            );
        }
    }

    private static void configureConnection(
            Connection connection
    ) throws SQLException {

        try (Statement statement =
                     connection.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");

            statement.execute("PRAGMA busy_timeout = 5000");
        }
    }

    public static String getDatabasePath() {

        return DATABASE_PATH.toString();
    }
}