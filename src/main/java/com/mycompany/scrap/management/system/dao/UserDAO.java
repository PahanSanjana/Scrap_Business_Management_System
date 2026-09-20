package com.mycompany.scrap.management.system.dao;

import com.mycompany.scrap.management.system.database.DatabaseConnection;
import com.mycompany.scrap.management.system.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    /**
     * Inserts a new user into the database.
     *
     * Note:
     * The password should be a securely generated hash.
     * Do not store plain-text passwords.
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

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getFullName());
            statement.setInt(4, user.getRoleId());
            statement.setBoolean(5, user.isActive());

            int rowsInserted = statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException e) {

            System.out.println("Error creating user.");
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Finds a user by username.
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

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    User user = new User();

                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPassword(
                            resultSet.getString("password_hash")
                    );
                    user.setFullName(
                            resultSet.getString("full_name")
                    );
                    user.setRoleId(resultSet.getInt("role_id"));
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

        return null;
    }
}