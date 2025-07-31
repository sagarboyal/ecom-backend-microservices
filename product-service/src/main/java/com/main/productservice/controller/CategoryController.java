package com.main.productservice.controller;

import com.main.productservice.constrains.AppConstraints;
import com.main.productservice.payloads.dtos.CategoryDTO;
import com.main.productservice.payloads.response.PageResponse;
import com.main.productservice.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping()
    public ResponseEntity<PageResponse<CategoryDTO>> getCategories(
            @RequestParam(name = "pageNumber", defaultValue = AppConstraints.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstraints.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstraints.DEFAULT_SORT_BY_CATEGORY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstraints.SORT_DIR, required = false) String sortOrder) {
        return ResponseEntity.ok(categoryService.getCategoryList(pageNumber, pageSize, sortBy, sortOrder));
    }

    @PostMapping()
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO category) {
        CategoryDTO data = categoryService.createCategory(category);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(data);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        CategoryDTO data = categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("category deleted successfully, with name: " + data.getCategoryName());
    }

    @PutMapping
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO category) {
        CategoryDTO data = categoryService.updateCategory(category);
        return ResponseEntity.ok(data);
    }
}
