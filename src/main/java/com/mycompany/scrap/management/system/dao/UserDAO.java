package com.mycompany.scrap.management.system.dao;

import com.mycompany.scrap.management.system.database.DatabaseConnection;
import com.mycompany.scrap.management.system.model.User;
import com.mycompany.scrap.management.system.security.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Create a new user in the database.
     *
     * The password is hashed before being stored.
     *
     * @param user User object containing the user's details
     * @return true if the user was created successfully, otherwise false
     */
    public boolean createUser(User user) {

        String sql = """
                INSERT INTO users (
                    username,
                    password_hash,
                    full_name,
                    role_id,
                    is_active
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Set the username
            statement.setString(1, user.getUsername());

            // Hash the password before storing it
            String hashedPassword =
                    PasswordUtil.hashPassword(user.getPassword());

            statement.setString(2, hashedPassword);

            // Set the user's full name
            statement.setString(3, user.getFullName());

            // Set the user's role ID
            statement.setInt(4, user.getRoleId());

            // Set the account active status
            statement.setBoolean(5, user.isActive());

            // Execute the INSERT query
            int rowsInserted = statement.executeUpdate();

            // Return true if a record was inserted
            return rowsInserted > 0;

        } catch (SQLException e) {

            System.out.println("Error creating user.");
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Find a user by their username.
     *
     * @param username Username to search for
     * @return User object if found, otherwise null
     */
    public User findByUsername(String username) {

        String sql = """
                SELECT
                    id,
                    username,
                    password_hash,
                    full_name,
                    role_id,
                    is_active,
                    created_at,
                    updated_at
                FROM users
                WHERE username = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Set the username parameter
            statement.setString(1, username);

            // Execute the SELECT query
            try (ResultSet resultSet = statement.executeQuery()) {

                // Check whether a matching user exists
                if (resultSet.next()) {

                    User user = new User();

                    // Read user information from the database
                    user.setId(
                            resultSet.getInt("id")
                    );

                    user.setUsername(
                            resultSet.getString("username")
                    );

                    /*
                     * The password field contains the stored hash,
                     * not the original plain-text password.
                     */
                    user.setPassword(
                            resultSet.getString("password_hash")
                    );

                    user.setFullName(
                            resultSet.getString("full_name")
                    );

                    user.setRoleId(
                            resultSet.getInt("role_id")
                    );

                    user.setActive(
                            resultSet.getBoolean("is_active")
                    );

                    return user;
                }
            }

        } catch (SQLException e) {

            System.out.println("Error finding user.");
            e.printStackTrace();
        }

        // Return null when the user is not found
        return null;
    }
}