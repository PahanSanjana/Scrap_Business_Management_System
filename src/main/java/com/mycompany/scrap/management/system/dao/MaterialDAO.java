package com.mycompany.scrap.management.system.dao;

import com.mycompany.scrap.management.system.database.DatabaseConnection;
import com.mycompany.scrap.management.system.model.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {

    // =========================================================
    // CREATE MATERIAL
    // =========================================================

    /**
     * Creates a new material in the database.
     *
     * Saves:
     * - Category ID
     * - Material name
     * - Description
     * - Measurement unit
     * - Minimum stock
     * - Active status
     *
     * @param material material object to save
     * @return true if saved successfully, otherwise false
     */
    public boolean createMaterial(
            Material material
    ) {

        if (material == null) {

            return false;
        }

        String sql = """
                INSERT INTO materials (
                    category_id,
                    material_name,
                    description,
                    measurement_unit,
                    minimum_stock,
                    is_active
                )
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setInt(
                    1,
                    material.getCategoryId()
            );

            statement.setString(
                    2,
                    cleanText(material.getMaterialName())
            );

            statement.setString(
                    3,
                    cleanText(material.getDescription())
            );

            statement.setString(
                    4,
                    cleanText(material.getMeasurementUnit())
            );

            statement.setDouble(
                    5,
                    material.getMinimumStock()
            );

            statement.setBoolean(
                    6,
                    material.isActive()
            );

            int affectedRows =
                    statement.executeUpdate();

            if (affectedRows == 0) {

                return false;
            }

            try (
                    ResultSet generatedKeys =
                            statement.getGeneratedKeys()
            ) {

                if (generatedKeys.next()) {

                    material.setId(
                            generatedKeys.getInt(1)
                    );
                }
            }

            return true;

        } catch (SQLException exception) {

            System.err.println(
                    "Error creating material: "
                    + exception.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // GET ALL MATERIALS
    // =========================================================

    /**
     * Retrieves all materials with their category names.
     *
     * Uses a JOIN so the application can display the category
     * name instead of only the category ID.
     *
     * @return list of all materials
     */
    public List<Material> getAllMaterials() {

        List<Material> materials =
                new ArrayList<>();

        String sql = """
                SELECT
                    m.id,
                    m.category_id,
                    c.category_name AS category_name,
                    m.material_name,
                    m.description,
                    m.measurement_unit,
                    m.minimum_stock,
                    m.is_active,
                    m.created_at,
                    m.updated_at
                FROM materials m
                INNER JOIN categories c
                    ON m.category_id = c.id
                ORDER BY m.id DESC
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

                Material material =
                        mapResultSetToMaterial(
                                resultSet
                        );

                materials.add(material);
            }

        } catch (SQLException exception) {

            System.err.println(
                    "Error loading materials: "
                    + exception.getMessage()
            );
        }

        return materials;
    }

    // =========================================================
    // SEARCH MATERIALS
    // =========================================================

    /**
     * Searches materials by:
     * - Material name
     * - Description
     * - Measurement unit
     * - Category name
     *
     * @param keyword search keyword
     * @return matching materials
     */
    public List<Material> searchMaterials(
            String keyword
    ) {

        List<Material> materials =
                new ArrayList<>();

        String searchKeyword =
                keyword == null
                        ? ""
                        : keyword.trim();

        if (searchKeyword.isEmpty()) {

            return getAllMaterials();
        }

        String sql = """
                SELECT
                    m.id,
                    m.category_id,
                    c.category_name AS category_name,
                    m.material_name,
                    m.description,
                    m.measurement_unit,
                    m.minimum_stock,
                    m.is_active,
                    m.created_at,
                    m.updated_at
                FROM materials m
                INNER JOIN categories c
                    ON m.category_id = c.id
                WHERE
                    LOWER(m.material_name)
                        LIKE LOWER(?)
                    OR LOWER(COALESCE(m.description, ''))
                        LIKE LOWER(?)
                    OR LOWER(m.measurement_unit)
                        LIKE LOWER(?)
                    OR LOWER(c.category_name)
                        LIKE LOWER(?)
                ORDER BY m.id DESC
                """;

        String searchPattern =
                "%" + searchKeyword + "%";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    searchPattern
            );

            statement.setString(
                    2,
                    searchPattern
            );

            statement.setString(
                    3,
                    searchPattern
            );

            statement.setString(
                    4,
                    searchPattern
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Material material =
                            mapResultSetToMaterial(
                                    resultSet
                            );

                    materials.add(material);
                }
            }

        } catch (SQLException exception) {

            System.err.println(
                    "Error searching materials: "
                    + exception.getMessage()
            );
        }

        return materials;
    }

    // =========================================================
    // UPDATE MATERIAL
    // =========================================================

    /**
     * Updates all editable material information.
     *
     * Updates:
     * - Category ID
     * - Material name
     * - Description
     * - Measurement unit
     * - Minimum stock
     * - Updated timestamp
     *
     * The existing active status is preserved.
     *
     * @param material material object to update
     * @return true if updated successfully, otherwise false
     */
    public boolean updateMaterial(
            Material material
    ) {

        if (material == null
                || material.getId() <= 0) {

            return false;
        }

        String sql = """
                UPDATE materials
                SET
                    category_id = ?,
                    material_name = ?,
                    description = ?,
                    measurement_unit = ?,
                    minimum_stock = ?,
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
                    material.getCategoryId()
            );

            statement.setString(
                    2,
                    cleanText(material.getMaterialName())
            );

            statement.setString(
                    3,
                    cleanText(material.getDescription())
            );

            statement.setString(
                    4,
                    cleanText(material.getMeasurementUnit())
            );

            statement.setDouble(
                    5,
                    material.getMinimumStock()
            );

            statement.setInt(
                    6,
                    material.getId()
            );

            int affectedRows =
                    statement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException exception) {

            System.err.println(
                    "Error updating material: "
                    + exception.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // UPDATE MATERIAL STATUS
    // =========================================================

    /**
     * Activates or deactivates a material.
     *
     * @param materialId material ID
     * @param active new active status
     * @return true if updated successfully, otherwise false
     */
    public boolean updateMaterialStatus(
            int materialId,
            boolean active
    ) {

        if (materialId <= 0) {

            return false;
        }

        String sql = """
                UPDATE materials
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

            statement.setBoolean(
                    1,
                    active
            );

            statement.setInt(
                    2,
                    materialId
            );

            int affectedRows =
                    statement.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException exception) {

            System.err.println(
                    "Error updating material status: "
                    + exception.getMessage()
            );

            return false;
        }
    }

    // =========================================================
    // FIND MATERIAL BY ID
    // =========================================================

    /**
     * Finds one material by its ID.
     *
     * @param materialId material ID
     * @return material object or null if not found
     */
    public Material findById(
            int materialId
    ) {

        if (materialId <= 0) {

            return null;
        }

        String sql = """
                SELECT
                    m.id,
                    m.category_id,
                    c.category_name AS category_name,
                    m.material_name,
                    m.description,
                    m.measurement_unit,
                    m.minimum_stock,
                    m.is_active,
                    m.created_at,
                    m.updated_at
                FROM materials m
                INNER JOIN categories c
                    ON m.category_id = c.id
                WHERE m.id = ?
                """;

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    materialId
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapResultSetToMaterial(
                            resultSet
                    );
                }
            }

        } catch (SQLException exception) {

            System.err.println(
                    "Error finding material: "
                    + exception.getMessage()
            );
        }

        return null;
    }

    // =========================================================
    // GET MATERIALS BY CATEGORY
    // =========================================================

    /**
     * Retrieves all materials belonging to a category.
     *
     * @param categoryId category ID
     * @return materials for the selected category
     */
    public List<Material> getMaterialsByCategoryId(
            int categoryId
    ) {

        List<Material> materials =
                new ArrayList<>();

        if (categoryId <= 0) {

            return materials;
        }

        String sql = """
                SELECT
                    m.id,
                    m.category_id,
                    c.category_name AS category_name,
                    m.material_name,
                    m.description,
                    m.measurement_unit,
                    m.minimum_stock,
                    m.is_active,
                    m.created_at,
                    m.updated_at
                FROM materials m
                INNER JOIN categories c
                    ON m.category_id = c.id
                WHERE m.category_id = ?
                ORDER BY m.material_name ASC
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

                while (resultSet.next()) {

                    Material material =
                            mapResultSetToMaterial(
                                    resultSet
                            );

                    materials.add(material);
                }
            }

        } catch (SQLException exception) {

            System.err.println(
                    "Error loading materials by category: "
                    + exception.getMessage()
            );
        }

        return materials;
    }

    // =========================================================
    // CHECK DUPLICATE MATERIAL
    // =========================================================

    /**
     * Checks whether a material name already exists
     * inside a category.
     *
     * This is useful because the database contains:
     *
     * UNIQUE (category_id, material_name)
     *
     * @param categoryId category ID
     * @param materialName material name
     * @param excludeMaterialId existing material ID to exclude
     * @return true if a duplicate exists
     */
    public boolean materialExists(
            int categoryId,
            String materialName,
            int excludeMaterialId
    ) {

        if (categoryId <= 0
                || materialName == null
                || materialName.trim().isEmpty()) {

            return false;
        }

        String sql = """
                SELECT COUNT(*)
                FROM materials
                WHERE
                    category_id = ?
                    AND LOWER(TRIM(material_name))
                        = LOWER(TRIM(?))
                    AND id != ?
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

            statement.setString(
                    2,
                    materialName.trim()
            );

            statement.setInt(
                    3,
                    Math.max(excludeMaterialId, 0)
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getInt(1) > 0;
                }
            }

        } catch (SQLException exception) {

            System.err.println(
                    "Error checking duplicate material: "
                    + exception.getMessage()
            );
        }

        return false;
    }

    // =========================================================
    // MAP RESULT SET TO MATERIAL
    // =========================================================

    /**
     * Maps database columns to the Material model.
     *
     * Uses explicit column names to prevent data
     * appearing in the wrong table columns.
     */
    private Material mapResultSetToMaterial(
            ResultSet resultSet
    ) throws SQLException {

        Material material =
                new Material();

        material.setId(
                resultSet.getInt("id")
        );

        material.setCategoryId(
                resultSet.getInt("category_id")
        );

        material.setCategoryName(
                resultSet.getString("category_name")
        );

        material.setMaterialName(
                resultSet.getString("material_name")
        );

        material.setDescription(
                resultSet.getString("description")
        );

        material.setMeasurementUnit(
                resultSet.getString("measurement_unit")
        );

        material.setMinimumStock(
                resultSet.getDouble("minimum_stock")
        );

        material.setActive(
                resultSet.getBoolean("is_active")
        );

        material.setCreatedAt(
                resultSet.getString("created_at")
        );

        material.setUpdatedAt(
                resultSet.getString("updated_at")
        );

        return material;
    }

    // =========================================================
    // CLEAN TEXT VALUES
    // =========================================================

    /**
     * Converts null text values to empty strings
     * before saving them to the database.
     */
    private String cleanText(
            String value
    ) {

        return value == null
                ? ""
                : value.trim();
    }
}