package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private List<Category> categories = new ArrayList<>();

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public String createCategory(Category category) {

        for (Category existingCategory : categories) {
            if (existingCategory.getCategoryId().equals(category.getCategoryId())) {
                return "Category already exists";
            }
        }
        categories.add(category);
        return "Category added successfully";
    }

    @Override
    public String deleteCategory(Long categoryId) {
        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElse(null);
        if (category == null) {
            return "Category not found";
        }
        categories.remove(category);
        return "Category deleted successfully";
    }

    @Override
    public String updateCategory(Long categoryId, Category category) {

        for (Category existingCategory : categories) {
            if (existingCategory.getCategoryId().equals(categoryId)) {
                existingCategory.setCategoryName(category.getCategoryName());
                return "Category updated successfully";
            }
        }

        return "Category not found";
    }
}
