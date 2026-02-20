package com.gonku.pos_be.service;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.dto.category.CategoryMapper;
import com.gonku.pos_be.dto.category.CategoryRequestDto;
import com.gonku.pos_be.dto.category.CategoryResponseDto;
import com.gonku.pos_be.dto.category.CategoryUpdateRequestDto;
import com.gonku.pos_be.dto.common.PageRequestDto;
import com.gonku.pos_be.dto.common.PageResponseDto;
import com.gonku.pos_be.entity.category.Category;
import com.gonku.pos_be.exception.ResourceNotFoundException;
import com.gonku.pos_be.repository.CategoryRepository;
import com.gonku.pos_be.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryValidationService categoryValidationService;
    private static final String ENTITY_NOT_FOUND = "Category";

    public PageResponseDto<CategoryResponseDto> findAll(PageRequestDto pageRequestDto) {
        Pageable pageable = PageUtil.createPageable(pageRequestDto);
        Page<Category> categoryPage = categoryRepository.findAllWithPagination(
                pageRequestDto.getSearch(),
                pageRequestDto.getIsActive(),
                pageable);
        List<CategoryResponseDto> responseDtoList = categoryPage.stream()
                .map(categoryMapper::toDto)
                .toList();

        return PageUtil.createPageResponse(responseDtoList, pageable, categoryPage.getTotalElements());
    }

    public CategoryResponseDto findById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND))));
    }

    public CategoryResponseDto add(CategoryRequestDto requestDto) {
        categoryValidationService
                .validateCategoryUniqueness(
                        requestDto.getName(),
                        null
                );

        requestDto.setIsActive(true);

        return categoryMapper.toDto(categoryRepository.save(categoryMapper.toEntity(requestDto)));
    }

    public CategoryResponseDto edit(Long id, CategoryUpdateRequestDto requestDto) {
        Category category = categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND)));

        categoryValidationService
                .validateCategoryUniqueness(
                        requestDto.getName(),
                        id
                );


        categoryMapper.updateFromDto(requestDto, category);
        Category updatedCategory = categoryRepository.save(category);

        return categoryMapper.toDto(updatedCategory);
    }

    public void remove(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND)));

        category.setIsActive(false);

        categoryRepository.save(category);
    }
}
