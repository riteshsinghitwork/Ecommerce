package com.ecommerce.project.service;

import com.ecommerce.project.model.Category;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public Category updateCategory(Long categoryId, Category category) {

        Optional<Category> existingCategoryOptional = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst();

        if (existingCategoryOptional.isPresent()) {
            Category existingCategory = existingCategoryOptional.get();
            existingCategory.setCategoryName(category.getCategoryName());
            return existingCategory;
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Category not found");
    }
}
