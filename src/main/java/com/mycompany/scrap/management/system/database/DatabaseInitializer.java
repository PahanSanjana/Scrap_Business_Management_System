package com.mycompany.scrap.management.system.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DatabaseInitializer {

    // =========================================================
    // PRIVATE CONSTRUCTOR
    // =========================================================

    private DatabaseInitializer() {

        // Prevent creating objects from this utility class.
    }

    // =========================================================
    // INITIALIZE DATABASE
    // =========================================================

    public static void initializeDatabase()
            throws SQLException {

        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {

            // Enable SQLite foreign-key enforcement
            enableForeignKeys(connection);

            // Start transaction
            connection.setAutoCommit(false);

            try {

                // Create database tables
                createRolesTable(connection);

                createUsersTable(connection);

                createCategoriesTable(connection);

                createMaterialsTable(connection);

                createCustomersTable(connection);

                // Create database indexes
                createDatabaseIndexes(connection);

                // Commit all changes
                connection.commit();

                System.out.println(
                        "Database tables initialized successfully!"
                );

            } catch (SQLException exception) {

                // Roll back all changes if initialization fails
                connection.rollback();

                System.out.println(
                        "Database initialization failed. "
                        + "Changes were rolled back."
                );

                throw exception;
            }
        }
    }

    // =========================================================
    // ENABLE FOREIGN KEYS
    // =========================================================

    private static void enableForeignKeys(
            Connection connection
    ) throws SQLException {

        String sql =
                "PRAGMA foreign_keys = ON";

        executeSql(
                connection,
                sql
        );
    }

    // =========================================================
    // CREATE ROLES TABLE
    // =========================================================

    private static void createRolesTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS roles (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    role_name TEXT NOT NULL UNIQUE,

                    description TEXT,

                    created_at TEXT NOT NULL
                        DEFAULT CURRENT_TIMESTAMP
                );
                """;

        executeSql(
                connection,
                sql
        );
    }

    // =========================================================
    // CREATE USERS TABLE
    // =========================================================

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

                    is_active INTEGER NOT NULL
                        DEFAULT 1,

                    created_at TEXT NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

                    updated_at TEXT,

                    FOREIGN KEY (role_id)
                        REFERENCES roles(id)
                );
                """;

        executeSql(
                connection,
                sql
        );
    }

    // =========================================================
    // CREATE CATEGORIES TABLE
    // =========================================================

    private static void createCategoriesTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS categories (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    category_name TEXT NOT NULL UNIQUE,

                    description TEXT,

                    is_active INTEGER NOT NULL
                        DEFAULT 1,

                    created_at TEXT NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

                    updated_at TEXT
                );
                """;

        executeSql(
                connection,
                sql
        );
    }

    // =========================================================
    // CREATE MATERIALS TABLE
    // =========================================================

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

                    minimum_stock REAL NOT NULL
                        DEFAULT 0,

                    is_active INTEGER NOT NULL
                        DEFAULT 1,

                    created_at TEXT NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

                    updated_at TEXT,

                    FOREIGN KEY (category_id)
                        REFERENCES categories(id),

                    UNIQUE (
                        category_id,
                        material_name
                    )
                );
                """;

        executeSql(
                connection,
                sql
        );
    }

    // =========================================================
    // CREATE CUSTOMERS TABLE
    // =========================================================

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

                    is_active INTEGER NOT NULL
                        DEFAULT 1,

                    created_at TEXT NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

                    updated_at TEXT
                );
                """;

        executeSql(
                connection,
                sql
        );
    }

    // =========================================================
    // CREATE DATABASE INDEXES
    // =========================================================

    private static void createDatabaseIndexes(
            Connection connection
    ) throws SQLException {

        // Index for faster user role searches
        String userRoleIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_users_role_id
                ON users(role_id);
                """;

        executeSql(
                connection,
                userRoleIndex
        );

        // Index for faster material category searches
        String materialCategoryIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_materials_category_id
                ON materials(category_id);
                """;

        executeSql(
                connection,
                materialCategoryIndex
        );

        // Index for faster active material searches
        String materialActiveIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_materials_is_active
                ON materials(is_active);
                """;

        executeSql(
                connection,
                materialActiveIndex
        );

        // Index for faster category status searches
        String categoryActiveIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_categories_is_active
                ON categories(is_active);
                """;

        executeSql(
                connection,
                categoryActiveIndex
        );

        // Index for faster customer status searches
        String customerActiveIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_customers_is_active
                ON customers(is_active);
                """;

        executeSql(
                connection,
                customerActiveIndex
        );
    }

    // =========================================================
    // EXECUTE SQL
    // =========================================================

    private static void executeSql(
            Connection connection,
            String sql
    ) throws SQLException {

        try (
                Statement statement =
                        connection.createStatement()
        ) {

            statement.executeUpdate(sql);
        }
    }
}