
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

                createSuppliersTable(connection);

                createPurchasesTable(connection);

                createPurchaseItemsTable(connection);

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
    // CREATE SUPPLIERS TABLE
    // =========================================================

    private static void createSuppliersTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS suppliers (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    supplier_code TEXT NOT NULL UNIQUE,

                    supplier_name TEXT NOT NULL,

                    contact_person TEXT,

                    phone_number TEXT NOT NULL,

                    email TEXT,

                    address TEXT,

                    payment_terms TEXT,

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
    // CREATE PURCHASES TABLE
    // =========================================================

    private static void createPurchasesTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS purchases (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    purchase_code TEXT NOT NULL UNIQUE,

                    supplier_id INTEGER NOT NULL,

                    purchase_date TEXT NOT NULL,

                    invoice_number TEXT,

                    payment_method TEXT,

                    payment_status TEXT NOT NULL
                        DEFAULT 'PENDING',

                    total_amount REAL NOT NULL
                        DEFAULT 0,

                    notes TEXT,

                    created_by INTEGER,

                    created_at TEXT NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

                    updated_at TEXT,

                    FOREIGN KEY (supplier_id)
                        REFERENCES suppliers(id),

                    FOREIGN KEY (created_by)
                        REFERENCES users(id)
                );
                """;

        executeSql(
                connection,
                sql
        );
    }

    // =========================================================
    // CREATE PURCHASE ITEMS TABLE
    // =========================================================

    private static void createPurchaseItemsTable(
            Connection connection
    ) throws SQLException {

        String sql = """
                CREATE TABLE IF NOT EXISTS purchase_items (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,

                    purchase_id INTEGER NOT NULL,

                    material_id INTEGER NOT NULL,

                    quantity REAL NOT NULL,

                    unit_price REAL NOT NULL,

                    total_price REAL NOT NULL,

                    created_at TEXT NOT NULL
                        DEFAULT CURRENT_TIMESTAMP,

                    FOREIGN KEY (purchase_id)
                        REFERENCES purchases(id)
                        ON DELETE CASCADE,

                    FOREIGN KEY (material_id)
                        REFERENCES materials(id)
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

        // Index for faster supplier status searches
        String supplierActiveIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_suppliers_is_active
                ON suppliers(is_active);
                """;

        executeSql(
                connection,
                supplierActiveIndex
        );

        // Index for faster purchase supplier searches
        String purchaseSupplierIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_purchases_supplier_id
                ON purchases(supplier_id);
                """;

        executeSql(
                connection,
                purchaseSupplierIndex
        );

        // Index for faster purchase date searches
        String purchaseDateIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_purchases_purchase_date
                ON purchases(purchase_date);
                """;

        executeSql(
                connection,
                purchaseDateIndex
        );

        // Index for faster purchase item searches
        String purchaseItemPurchaseIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_purchase_items_purchase_id
                ON purchase_items(purchase_id);
                """;

        executeSql(
                connection,
                purchaseItemPurchaseIndex
        );

        // Index for faster material purchase searches
        String purchaseItemMaterialIndex = """
                CREATE INDEX IF NOT EXISTS
                idx_purchase_items_material_id
                ON purchase_items(material_id);
                """;

        executeSql(
                connection,
                purchaseItemMaterialIndex
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