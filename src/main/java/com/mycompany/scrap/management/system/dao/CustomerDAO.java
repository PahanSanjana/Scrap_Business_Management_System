
package com.mycompany.scrap.management.system.dao;

import com.mycompany.scrap.management.system.database.DatabaseConnection;
import com.mycompany.scrap.management.system.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // Create a new customer
    public boolean createCustomer(Customer customer) throws SQLException {

        String sql = """
                INSERT INTO customers
                (customer_code, full_name, nic_number, phone_number, address, is_active)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getCustomerCode());
            statement.setString(2, customer.getFullName());
            statement.setString(3, customer.getNicNumber());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getAddress());
            statement.setInt(6, customer.isActive() ? 1 : 0);

            return statement.executeUpdate() > 0;
        }
    }

    // Get all customers
    public List<Customer> getAllCustomers() throws SQLException {

        List<Customer> customers = new ArrayList<>();

        String sql = """
                SELECT id, customer_code, full_name, nic_number,
                       phone_number, address, is_active
                FROM customers
                ORDER BY id DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                customers.add(mapResultSetToCustomer(resultSet));
            }
        }

        return customers;
    }

    // Search customers
    public List<Customer> searchCustomers(String searchText) throws SQLException {

        List<Customer> customers = new ArrayList<>();

        String sql = """
                SELECT id, customer_code, full_name, nic_number,
                       phone_number, address, is_active
                FROM customers
                WHERE customer_code LIKE ?
                   OR full_name LIKE ?
                   OR nic_number LIKE ?
                   OR phone_number LIKE ?
                ORDER BY id DESC
                """;

        String searchPattern = "%" + searchText + "%";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);
            statement.setString(3, searchPattern);
            statement.setString(4, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    customers.add(mapResultSetToCustomer(resultSet));
                }
            }
        }

        return customers;
    }

    // Update customer details
    public boolean updateCustomer(Customer customer) throws SQLException {

        String sql = """
                UPDATE customers
                SET customer_code = ?,
                    full_name = ?,
                    nic_number = ?,
                    phone_number = ?,
                    address = ?,
                    is_active = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customer.getCustomerCode());
            statement.setString(2, customer.getFullName());
            statement.setString(3, customer.getNicNumber());
            statement.setString(4, customer.getPhoneNumber());
            statement.setString(5, customer.getAddress());
            statement.setInt(6, customer.isActive() ? 1 : 0);
            statement.setInt(7, customer.getId());

            return statement.executeUpdate() > 0;
        }
    }

    // Activate or deactivate a customer
    public boolean updateCustomerStatus(int customerId, boolean active)
            throws SQLException {

        String sql = """
                UPDATE customers
                SET is_active = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, active ? 1 : 0);
            statement.setInt(2, customerId);

            return statement.executeUpdate() > 0;
        }
    }

    // Check whether a customer code already exists
    public boolean customerCodeExists(String customerCode)
            throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM customers
                WHERE customer_code = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customerCode);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    // Check customer code while excluding a specific customer ID
    public boolean customerCodeExists(String customerCode, int excludeId)
            throws SQLException {

        String sql = """
                SELECT COUNT(*)
                FROM customers
                WHERE customer_code = ?
                  AND id != ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, customerCode);
            statement.setInt(2, excludeId);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    // Convert database result into Customer object
    private Customer mapResultSetToCustomer(ResultSet resultSet)
            throws SQLException {

        return new Customer(
                resultSet.getInt("id"),
                resultSet.getString("customer_code"),
                resultSet.getString("full_name"),
                resultSet.getString("nic_number"),
                resultSet.getString("phone_number"),
                resultSet.getString("address"),
                resultSet.getInt("is_active") == 1
        );
    }
}