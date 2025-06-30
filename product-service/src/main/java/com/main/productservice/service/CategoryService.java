package com.main.productservice.service;

import com.main.productservice.payloads.dtos.CategoryDTO;
import com.main.productservice.payloads.response.PageResponse;
public interface CategoryService {
    PageResponse<CategoryDTO> getCategoryList(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO getCategoryById(String id);
    CategoryDTO createCategory(CategoryDTO categoryDto);
    CategoryDTO deleteCategoryById(String id);
    CategoryDTO updateCategory(CategoryDTO categoryDto);
}
