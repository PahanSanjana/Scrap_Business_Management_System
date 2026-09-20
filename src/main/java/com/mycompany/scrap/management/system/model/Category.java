package com.mycompany.scrap.management.system.model;

public class Category {

    private int id;
    private String categoryName;
    private String description;
    private boolean active;

    public Category() {
    }

    public Category(
            String categoryName,
            String description,
            boolean active
    ) {

        this.categoryName = categoryName;
        this.description = description;
        this.active = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(
            String categoryName
    ) {

        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
            String description
    ) {

        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(
            boolean active
    ) {

        this.active = active;
    }

    @Override
    public String toString() {

        return "Category{"
                + "id=" + id
                + ", categoryName='"
                + categoryName + '\''
                + ", description='"
                + description + '\''
                + ", active=" + active
                + '}';
    }
}