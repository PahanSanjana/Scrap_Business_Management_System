package com.mycompany.scrap.management.system.model;

public class Material {

    // =========================================================
    // FIELDS
    // =========================================================

    private int id;

    private int categoryId;

    // Category name is retrieved using JOIN from the database
    private String categoryName;

    private String materialName;

    private String description;

    private String measurementUnit;

    private double minimumStock;

    private boolean active;

    private String createdAt;

    private String updatedAt;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Material() {
    }

    // =========================================================
    // CONSTRUCTOR FOR CREATING MATERIAL
    // =========================================================

    public Material(
            int categoryId,
            String materialName,
            String description,
            String measurementUnit,
            double minimumStock
    ) {

        this.categoryId = categoryId;

        this.materialName = materialName;

        this.description = description;

        this.measurementUnit = measurementUnit;

        this.minimumStock = minimumStock;

        this.active = true;
    }

    // =========================================================
    // GETTERS AND SETTERS
    // =========================================================

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    // =========================================================
    // CATEGORY ID
    // =========================================================

    public int getCategoryId() {

        return categoryId;
    }

    public void setCategoryId(int categoryId) {

        this.categoryId = categoryId;
    }

    // =========================================================
    // CATEGORY NAME
    // =========================================================

    public String getCategoryName() {

        return categoryName;
    }

    public void setCategoryName(String categoryName) {

        this.categoryName = categoryName;
    }

    // =========================================================
    // MATERIAL NAME
    // =========================================================

    public String getMaterialName() {

        return materialName;
    }

    public void setMaterialName(String materialName) {

        this.materialName = materialName;
    }

    // =========================================================
    // DESCRIPTION
    // =========================================================

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    // =========================================================
    // MEASUREMENT UNIT
    // =========================================================

    public String getMeasurementUnit() {

        return measurementUnit;
    }

    public void setMeasurementUnit(String measurementUnit) {

        this.measurementUnit = measurementUnit;
    }

    // =========================================================
    // MINIMUM STOCK
    // =========================================================

    public double getMinimumStock() {

        return minimumStock;
    }

    public void setMinimumStock(double minimumStock) {

        this.minimumStock = minimumStock;
    }

    // =========================================================
    // ACTIVE STATUS
    // =========================================================

    public boolean isActive() {

        return active;
    }

    public void setActive(boolean active) {

        this.active = active;
    }

    // =========================================================
    // CREATED DATE
    // =========================================================

    public String getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(String createdAt) {

        this.createdAt = createdAt;
    }

    // =========================================================
    // UPDATED DATE
    // =========================================================

    public String getUpdatedAt() {

        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {

        this.updatedAt = updatedAt;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    @Override
    public String toString() {

        return "Material{"
                + "id=" + id
                + ", categoryId=" + categoryId
                + ", categoryName='" + categoryName + '\''
                + ", materialName='" + materialName + '\''
                + ", description='" + description + '\''
                + ", measurementUnit='" + measurementUnit + '\''
                + ", minimumStock=" + minimumStock
                + ", active=" + active
                + ", createdAt='" + createdAt + '\''
                + ", updatedAt='" + updatedAt + '\''
                + '}';
    }
}