package com.gonku.pos_be.controller;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.dto.common.PageRequestDto;
import com.gonku.pos_be.dto.common.PageResponseDto;
import com.gonku.pos_be.dto.product.ProductRequestDto;
import com.gonku.pos_be.dto.product.ProductResponseDto;
import com.gonku.pos_be.dto.product.ProductUpdateRequestDto;
import com.gonku.pos_be.service.ProductService;
import com.gonku.pos_be.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Validated
public class ProductController {
    private final ProductService productService;
    private static final String ENTITY_RESPONSE = "Product";
    private static final String PATH_WITH_ID = "/products/{id}";
    private static final String PATH_WITHOUT_ID = "/products";

    @GetMapping(PATH_WITHOUT_ID)
    public ResponseEntity<ApiResponse<PageResponseDto<ProductResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "true") Boolean isActive,
            @RequestParam(required = false) String search
    ) {
        PageRequestDto pageRequestDto = new PageRequestDto(page, size, sort, direction, isActive, search);
        PageResponseDto<ProductResponseDto> products = productService.findAll(pageRequestDto);
        ApiResponse<PageResponseDto<ProductResponseDto>> response = ApiResponse.success(ResponseMessages.SUCCESS, products);

        return ResponseEntity.ok(response);
    }

    @GetMapping(PATH_WITH_ID)
    public ResponseEntity<ApiResponse<ProductResponseDto>> getById(@PathVariable Long id) {
        ProductResponseDto product = productService.findById(id);
        ApiResponse<ProductResponseDto> response = ApiResponse.success(ResponseMessages.SUCCESS, product);

        return ResponseEntity.ok(response);
    }

    @PostMapping(PATH_WITHOUT_ID)
    public ResponseEntity<ApiResponse<ProductResponseDto>> create(@Valid @RequestBody ProductRequestDto requestDto) {
        ProductResponseDto createdProduct = productService.add(requestDto);
        ApiResponse<ProductResponseDto> response = ApiResponse.success(
                HttpStatus.CREATED.value(), ResponseMessages.created(ENTITY_RESPONSE), createdProduct);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProduct.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(PATH_WITH_ID)
    public ResponseEntity<ApiResponse<ProductResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequestDto requestDto
    ) {
        ProductResponseDto updatedProduct = productService.edit(id, requestDto);
        ApiResponse<ProductResponseDto> response = ApiResponse.success(ResponseMessages.updated(ENTITY_RESPONSE), updatedProduct);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(PATH_WITH_ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        productService.remove(id);
        ApiResponse<Void> response = ApiResponse.success(ResponseMessages.deleted(ENTITY_RESPONSE), null);

        return ResponseEntity.ok(response);
    }
}
