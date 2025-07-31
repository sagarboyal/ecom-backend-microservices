package com.main.productservice.serviceImpl;

import com.main.productservice.entity.Category;
import com.main.productservice.exceptions.custom.APIException;
import com.main.productservice.exceptions.custom.ResourceNotFoundException;
import com.main.productservice.payloads.dtos.CategoryDTO;
import com.main.productservice.payloads.response.PageResponse;
import com.main.productservice.repository.CategoryRepository;
import com.main.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public PageResponse<CategoryDTO> getCategoryList(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        Sort sort = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sort);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);
        List<Category> categoryList = categoryPage.getContent();

        if (categoryList.isEmpty())
            throw new APIException("No Categories Found!!!");

        List<CategoryDTO> categoryDTOList = categoryList.stream()
                .map(this::convertDTO)
                .toList();

        return PageResponse.<CategoryDTO>builder()
                .content(categoryDTOList)
                .pageNumber(categoryPage.getNumber())
                .pageSize(categoryPage.getSize())
                .totalElements(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .lastPage(categoryPage.isLast())
                .build();
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDto) {
        Category data = categoryRepository.findByCategoryName(categoryDto.getCategoryName());

        if (data != null) throw new APIException("Category already exists");

        Category category = convertDocument(categoryDto);
        return convertDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryDTO getCategoryById(String id) {
        Category data = categoryRepository
                .findById(String.valueOf(id))
                .orElseThrow(() ->
                            new ResourceNotFoundException("Category", "categoryId", id));

        return convertDTO(categoryRepository.save(data));
    }

    @Override
    public CategoryDTO deleteCategoryById(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", id));
        categoryRepository.delete(category);
        return convertDTO(category);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDto) {
        getCategoryById(categoryDto.getCategoryId());
        categoryRepository.save(convertDocument(categoryDto));
        return categoryDto;
    }

    private CategoryDTO convertDTO(Category category) {
        return CategoryDTO.builder()
                .categoryId(category.getCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }
    private Category convertDocument(CategoryDTO dto) {
        return new Category(dto.getCategoryId(), dto.getCategoryName());
    }
}
