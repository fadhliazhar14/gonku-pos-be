package com.gonku.pos_be.controller;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.dto.category.CategoryRequestDto;
import com.gonku.pos_be.dto.category.CategoryResponseDto;
import com.gonku.pos_be.dto.category.CategoryUpdateRequestDto;
import com.gonku.pos_be.dto.common.PageRequestDto;
import com.gonku.pos_be.dto.common.PageResponseDto;
import com.gonku.pos_be.entity.Category;
import com.gonku.pos_be.service.CategoryService;
import com.gonku.pos_be.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;
    private static final String ENTITY_RESPONSE = "Category";
    private static final String PATH_WITH_ID = "/categories/{id}";

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<PageResponseDto<CategoryResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "true") Boolean isActive,
            @RequestParam(required = false) String search
    ) {
        PageRequestDto pageRequestDto = new PageRequestDto(page, size, sort, direction, isActive, search);
        PageResponseDto<CategoryResponseDto> categories = categoryService.findAll(pageRequestDto);
        ApiResponse<PageResponseDto<CategoryResponseDto>> response = ApiResponse.success(ResponseMessages.SUCCESS, categories);

        return ResponseEntity.ok(response);
    }

    @GetMapping(PATH_WITH_ID)
    public ResponseEntity<ApiResponse<CategoryResponseDto>> getById(@PathVariable Long id) {
        CategoryResponseDto category = categoryService.findById(id);
        ApiResponse<CategoryResponseDto> response = ApiResponse.success(ResponseMessages.SUCCESS, category);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> create(@Valid @RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto createdCategory = categoryService.add(requestDto);
        ApiResponse<CategoryResponseDto> response = ApiResponse.success(
                HttpStatus.CREATED.value(), ResponseMessages.created(ENTITY_RESPONSE), createdCategory);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCategory.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<CategoryResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequestDto requestDto
            ) {
        CategoryResponseDto updatedCategory = categoryService.edit(id, requestDto);
        ApiResponse<CategoryResponseDto> response = ApiResponse.success(ResponseMessages.updated(ENTITY_RESPONSE), updatedCategory);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(PATH_WITH_ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        categoryService.remove(id);
        ApiResponse<Void> response = ApiResponse.success(ResponseMessages.deleted(ENTITY_RESPONSE), null);

        return ResponseEntity.ok(response);
    }
}
