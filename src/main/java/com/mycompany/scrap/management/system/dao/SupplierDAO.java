package com.mycompany.scrap.management.system.dao;

import com.mycompany.scrap.management.system.database.DatabaseConnection;
import com.mycompany.scrap.management.system.model.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Supplier management.
 *
 * Handles database operations related to suppliers.
 */
public class SupplierDAO {

    // =========================================================
    // CREATE SUPPLIER
    // =========================================================

    /**
     * Creates a new supplier.
     *
     * @param supplier supplier object
     * @return true if the supplier was created successfully
     * @throws SQLException if a database error occurs
     */
    public boolean createSupplier(
            Supplier supplier
    ) throws SQLException {

        String sql = """
                INSERT INTO suppliers (
                    supplier_code,
                    supplier_name,
                    contact_person,
                    phone_number,
                    email,
                    address,
                    payment_terms,
                    is_active
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    cleanText(supplier.getSupplierCode())
            );

            statement.setString(
                    2,
                    cleanText(supplier.getSupplierName())
            );

            statement.setString(
                    3,
                    cleanText(supplier.getContactPerson())
            );

            statement.setString(
                    4,
                    cleanText(supplier.getPhoneNumber())
            );

            statement.setString(
                    5,
                    cleanText(supplier.getEmail())
            );

            statement.setString(
                    6,
                    cleanText(supplier.getAddress())
            );

            statement.setString(
                    7,
                    cleanText(supplier.getPaymentTerms())
            );

            statement.setInt(
                    8,
                    supplier.isActive() ? 1 : 0
            );

            return statement.executeUpdate() > 0;
        }
    }

    // =========================================================
    // GET ALL SUPPLIERS
    // =========================================================

    /**
     * Retrieves all suppliers.
     *
     * @return list of suppliers
     * @throws SQLException if a database error occurs
     */
    public List<Supplier> getAllSuppliers()
            throws SQLException {

        List<Supplier> suppliers =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    supplier_code,
                    supplier_name,
                    contact_person,
                    phone_number,
                    email,
                    address,
                    payment_terms,
                    is_active,
                    created_at,
                    updated_at
                FROM suppliers
                ORDER BY id DESC;
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                suppliers.add(
                        mapResultSetToSupplier(resultSet)
                );
            }
        }

        return suppliers;
    }

    // =========================================================
    // SEARCH SUPPLIERS
    // =========================================================

    /**
     * Searches suppliers using multiple fields.
     *
     * @param searchText search keyword
     * @return matching suppliers
     * @throws SQLException if a database error occurs
     */
    public List<Supplier> searchSuppliers(
            String searchText
    ) throws SQLException {

        List<Supplier> suppliers =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    supplier_code,
                    supplier_name,
                    contact_person,
                    phone_number,
                    email,
                    address,
                    payment_terms,
                    is_active,
                    created_at,
                    updated_at
                FROM suppliers
                WHERE
                    supplier_code LIKE ?
                    OR supplier_name LIKE ?
                    OR contact_person LIKE ?
                    OR phone_number LIKE ?
                    OR email LIKE ?
                    OR address LIKE ?
                    OR payment_terms LIKE ?
                ORDER BY id DESC;
                """;

        String keyword =
                "%" + cleanText(searchText) + "%";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            for (int i = 1; i <= 7; i++) {

                statement.setString(
                        i,
                        keyword
                );
            }

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    suppliers.add(
                            mapResultSetToSupplier(resultSet)
                    );
                }
            }
        }

        return suppliers;
    }

    // =========================================================
    // UPDATE SUPPLIER
    // =========================================================

    /**
     * Updates an existing supplier.
     *
     * @param supplier supplier object
     * @return true if the supplier was updated successfully
     * @throws SQLException if a database error occurs
     */
    public boolean updateSupplier(
            Supplier supplier
    ) throws SQLException {

        String sql = """
                UPDATE suppliers
                SET
                    supplier_code = ?,
                    supplier_name = ?,
                    contact_person = ?,
                    phone_number = ?,
                    email = ?,
                    address = ?,
                    payment_terms = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?;
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    cleanText(supplier.getSupplierCode())
            );

            statement.setString(
                    2,
                    cleanText(supplier.getSupplierName())
            );

            statement.setString(
                    3,
                    cleanText(supplier.getContactPerson())
            );

            statement.setString(
                    4,
                    cleanText(supplier.getPhoneNumber())
            );

            statement.setString(
                    5,
                    cleanText(supplier.getEmail())
            );

            statement.setString(
                    6,
                    cleanText(supplier.getAddress())
            );

            statement.setString(
                    7,
                    cleanText(supplier.getPaymentTerms())
            );

            statement.setInt(
                    8,
                    supplier.getId()
            );

            return statement.executeUpdate() > 0;
        }
    }

    // =========================================================
    // UPDATE SUPPLIER STATUS
    // =========================================================

    /**
     * Activates or deactivates a supplier.
     *
     * @param supplierId supplier ID
     * @param active new supplier status
     * @return true if the status was updated successfully
     * @throws SQLException if a database error occurs
     */
    public boolean updateSupplierStatus(
            int supplierId,
            boolean active
    ) throws SQLException {

        String sql = """
                UPDATE suppliers
                SET
                    is_active = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?;
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    active ? 1 : 0
            );

            statement.setInt(
                    2,
                    supplierId
            );

            return statement.executeUpdate() > 0;
        }
    }

    // =========================================================
    // FIND SUPPLIER BY ID
    // =========================================================

    /**
     * Finds a supplier using its ID.
     *
     * @param supplierId supplier ID
     * @return supplier object or null if not found
     * @throws SQLException if a database error occurs
     */
    public Supplier findById(
            int supplierId
    ) throws SQLException {

        String sql = """
                SELECT
                    id,
                    supplier_code,
                    supplier_name,
                    contact_person,
                    phone_number,
                    email,
                    address,
                    payment_terms,
                    is_active,
                    created_at,
                    updated_at
                FROM suppliers
                WHERE id = ?;
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    supplierId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapResultSetToSupplier(
                            resultSet
                    );
                }
            }
        }

        return null;
    }

    // =========================================================
    // CHECK DUPLICATE SUPPLIER CODE
    // =========================================================

    /**
     * Checks whether a supplier code already exists.
     *
     * @param supplierCode supplier code
     * @return true if the code already exists
     * @throws SQLException if a database error occurs
     */
    public boolean supplierCodeExists(
            String supplierCode
    ) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM suppliers
                WHERE supplier_code = ?;
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    cleanText(supplierCode)
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    // =========================================================
    // CHECK DUPLICATE SUPPLIER CODE
    // EXCLUDING CURRENT SUPPLIER
    // =========================================================

    /**
     * Checks whether a supplier code exists for another supplier.
     *
     * Useful when updating an existing supplier.
     *
     * @param supplierCode supplier code
     * @param excludeSupplierId current supplier ID
     * @return true if another supplier uses the code
     * @throws SQLException if a database error occurs
     */
    public boolean supplierCodeExists(
            String supplierCode,
            int excludeSupplierId
    ) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM suppliers
                WHERE
                    supplier_code = ?
                    AND id != ?;
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    cleanText(supplierCode)
            );

            statement.setInt(
                    2,
                    excludeSupplierId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    // =========================================================
    // CHECK DUPLICATE SUPPLIER NAME
    // =========================================================

    /**
     * Checks whether a supplier name already exists.
     *
     * @param supplierName supplier name
     * @return true if the name already exists
     * @throws SQLException if a database error occurs
     */
    public boolean supplierNameExists(
            String supplierName
    ) throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM suppliers
                WHERE LOWER(TRIM(supplier_name))
                      = LOWER(TRIM(?));
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    cleanText(supplierName)
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    // =========================================================
    // MAP RESULT SET TO SUPPLIER
    // =========================================================

    /**
     * Converts a database row into a Supplier object.
     *
     * @param resultSet database result set
     * @return mapped Supplier object
     * @throws SQLException if a database error occurs
     */
    private Supplier mapResultSetToSupplier(
            ResultSet resultSet
    ) throws SQLException {

        Supplier supplier =
                new Supplier();

        supplier.setId(
                resultSet.getInt("id")
        );

        supplier.setSupplierCode(
                resultSet.getString("supplier_code")
        );

        supplier.setSupplierName(
                resultSet.getString("supplier_name")
        );

        supplier.setContactPerson(
                resultSet.getString("contact_person")
        );

        supplier.setPhoneNumber(
                resultSet.getString("phone_number")
        );

        supplier.setEmail(
                resultSet.getString("email")
        );

        supplier.setAddress(
                resultSet.getString("address")
        );

        supplier.setPaymentTerms(
                resultSet.getString("payment_terms")
        );

        supplier.setActive(
                resultSet.getInt("is_active") == 1
        );

        supplier.setCreatedAt(
                resultSet.getString("created_at")
        );

        supplier.setUpdatedAt(
                resultSet.getString("updated_at")
        );

        return supplier;
    }

    // =========================================================
    // CLEAN TEXT
    // =========================================================

    /**
     * Removes unnecessary spaces.
     *
     * Converts null values into empty strings.
     *
     * @param value input text
     * @return cleaned text
     */
    private String cleanText(
            String value
    ) {

        if (value == null) {

            return "";
        }

        return value.trim();
    }
}