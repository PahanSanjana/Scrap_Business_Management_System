
package com.mycompany.scrap.management.system.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    private DatabaseInitializer() {
        // Prevent creating objects from this utility class.
    }

    public static void initializeDatabase() throws SQLException {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            connection.setAutoCommit(false);

            try {
                createRolesTable(connection);
                createUsersTable(connection);
                createCategoriesTable(connection);
                createMaterialsTable(connection);
                createCustomersTable(connection);

                connection.commit();

                System.out.println(
                        "Database tables initialized successfully!"
                );

            } catch (SQLException exception) {

                connection.rollback();

                System.out.println(
                        "Database initialization failed. "
                        + "Changes were rolled back."
                );

                throw exception;
            }
        }
    }

    private static void createRolesTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS roles (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    role_name TEXT NOT NULL UNIQUE,
                    description TEXT,
                    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP
                );
                """;

        executeSql(connection, sql);
    }

    private static void createUsersTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    full_name TEXT NOT NULL,
                    role_id INTEGER NOT NULL,
                    is_active INTEGER NOT NULL DEFAULT 1,
                    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT,

                    FOREIGN KEY (role_id)
                        REFERENCES roles(id)
                );
                """;

        executeSql(connection, sql);
    }

    private static void createCategoriesTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS categories (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    category_name TEXT NOT NULL UNIQUE,
                    description TEXT,
                    is_active INTEGER NOT NULL DEFAULT 1,
                    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT
                );
                """;

        executeSql(connection, sql);
    }

    private static void createMaterialsTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS materials (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    category_id INTEGER NOT NULL,
                    material_name TEXT NOT NULL,
                    description TEXT,
                    measurement_unit TEXT NOT NULL,
                    minimum_stock REAL NOT NULL DEFAULT 0,
                    is_active INTEGER NOT NULL DEFAULT 1,
                    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT,

                    FOREIGN KEY (category_id)
                        REFERENCES categories(id),

                    UNIQUE (category_id, material_name)
                );
                """;

        executeSql(connection, sql);
    }

    private static void createCustomersTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS customers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    customer_code TEXT NOT NULL UNIQUE,
                    full_name TEXT NOT NULL,
                    nic_number TEXT,
                    phone_number TEXT NOT NULL,
                    address TEXT,
                    is_active INTEGER NOT NULL DEFAULT 1,
                    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    updated_at TEXT
                );
                """;

        executeSql(connection, sql);
    }

    private static void executeSql(
            Connection connection,
            String sql
    ) throws SQLException {

        try (Statement statement =
                     connection.createStatement()) {

            statement.executeUpdate(sql);
        }
    }
}