package com.ecommerce.project.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class Category {
    @NotNull(message = "Category ID is required")
    private Long categoryId;
    @NotBlank(message = "Category Name is required")
    private String categoryName;

    // Default constructor (Required)
    public Category() {
    }

    public Category(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}