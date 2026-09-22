package com.mycompany.scrap.management.system.model;

public class Category {

    // =========================================================
    // FIELDS
    // =========================================================

    private int id;

    private String categoryName;

    private String description;

    private boolean active;

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public Category() {
    }

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public Category(
            String categoryName,
            String description,
            boolean active
    ) {

        this.categoryName = categoryName;

        this.description = description;

        this.active = active;
    }

    // =========================================================
    // ID
    // =========================================================

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    // =========================================================
    // CATEGORY NAME
    // =========================================================

    public String getCategoryName() {

        return categoryName;
    }

    public void setCategoryName(
            String categoryName
    ) {

        this.categoryName = categoryName;
    }

    // =========================================================
    // DESCRIPTION
    // =========================================================

    public String getDescription() {

        return description;
    }

    public void setDescription(
            String description
    ) {

        this.description = description;
    }

    // =========================================================
    // ACTIVE STATUS
    // =========================================================

    public boolean isActive() {

        return active;
    }

    public void setActive(
            boolean active
    ) {

        this.active = active;
    }

    // =========================================================
    // TO STRING
    // =========================================================

    /*
     * This method controls how the category appears
     * inside the ComboBox and other JavaFX controls.
     *
     * It displays only the category name instead of
     * displaying the complete Category object.
     */

    @Override
    public String toString() {

        return categoryName == null
                ? ""
                : categoryName;
    }
}