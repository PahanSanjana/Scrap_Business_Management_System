
package com.mycompany.scrap.management.system.dao;

import com.mycompany.scrap.management.system.database.DatabaseConnection;
import com.mycompany.scrap.management.system.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // =========================================================
    // CREATE CATEGORY
    // =========================================================

    public boolean createCategory(Category category) {

        String sql = """
                INSERT INTO categories
                (
                    category_name,
                    description,
                    is_active
                )
                VALUES (?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    category.getCategoryName()
            );

            statement.setString(
                    2,
                    category.getDescription()
            );

            statement.setInt(
                    3,
                    category.isActive() ? 1 : 0
            );

            int rowsInserted =
                    statement.executeUpdate();

            return rowsInserted > 0;

        } catch (SQLException exception) {

            exception.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // GET ALL CATEGORIES
    // =========================================================

    public List<Category> getAllCategories() {

        List<Category> categories =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    category_name,
                    description,
                    is_active
                FROM categories
                ORDER BY id DESC
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

                Category category =
                        mapResultSetToCategory(resultSet);

                categories.add(category);
            }

        } catch (SQLException exception) {

            exception.printStackTrace();
        }

        return categories;
    }

    // =========================================================
    // SEARCH CATEGORIES
    // =========================================================

    public List<Category> searchCategories(
            String keyword
    ) {

        List<Category> categories =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    category_name,
                    description,
                    is_active
                FROM categories
                WHERE LOWER(category_name)
                    LIKE LOWER(?)
                   OR LOWER(description)
                    LIKE LOWER(?)
                ORDER BY id DESC
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            String searchKeyword =
                    "%" + keyword + "%";

            statement.setString(
                    1,
                    searchKeyword
            );

            statement.setString(
                    2,
                    searchKeyword
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Category category =
                            mapResultSetToCategory(
                                    resultSet
                            );

                    categories.add(category);
                }
            }

        } catch (SQLException exception) {

            exception.printStackTrace();
        }

        return categories;
    }

    // =========================================================
    // UPDATE CATEGORY
    // =========================================================

    public boolean updateCategory(
            Category category
    ) {

        String sql = """
                UPDATE categories
                SET
                    category_name = ?,
                    description = ?,
                    is_active = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    category.getCategoryName()
            );

            statement.setString(
                    2,
                    category.getDescription()
            );

            statement.setInt(
                    3,
                    category.isActive() ? 1 : 0
            );

            statement.setInt(
                    4,
                    category.getId()
            );

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException exception) {

            exception.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // UPDATE CATEGORY STATUS
    // =========================================================

    public boolean updateCategoryStatus(
            int categoryId,
            boolean active
    ) {

        String sql = """
                UPDATE categories
                SET
                    is_active = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
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
                    categoryId
            );

            int rowsUpdated =
                    statement.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException exception) {

            exception.printStackTrace();

            return false;
        }
    }

    // =========================================================
    // FIND CATEGORY BY ID
    // =========================================================

    public Category findById(
            int categoryId
    ) {

        String sql = """
                SELECT
                    id,
                    category_name,
                    description,
                    is_active
                FROM categories
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    categoryId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapResultSetToCategory(
                            resultSet
                    );
                }
            }

        } catch (SQLException exception) {

            exception.printStackTrace();
        }

        return null;
    }

    // =========================================================
    // CONVERT RESULT SET TO CATEGORY OBJECT
    // =========================================================

    private Category mapResultSetToCategory(
            ResultSet resultSet
    ) throws SQLException {

        Category category =
                new Category();

        category.setId(
                resultSet.getInt("id")
        );

        category.setCategoryName(
                resultSet.getString("category_name")
        );

        category.setDescription(
                resultSet.getString("description")
        );

        category.setActive(
                resultSet.getInt("is_active") == 1
        );

        return category;
    }
}