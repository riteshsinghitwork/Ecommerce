package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/public/categories")
    public String createCategory(@RequestBody Category category)
    {
        categoryService.createCategory(category);
        return "Category Added successfully";
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId)
    {
        boolean isDeleted = categoryService.deleteCategory(categoryId);
        if (isDeleted) {
            return "Category deleted successfully";
        }
        return "Category not found";
    }

    @PutMapping("/public/categories/{categoryId}")
    public String updateCategory(@PathVariable Long categoryId,
                                 @RequestBody Category category) {

        boolean isUpdated = categoryService.updateCategory(categoryId, category);

        if (isUpdated) {
            return "Category updated successfully";
        }

        return "Category not found";
    }
}
