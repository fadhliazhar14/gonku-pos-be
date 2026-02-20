package com.gonku.pos_be.service;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.dto.common.PageRequestDto;
import com.gonku.pos_be.dto.common.PageResponseDto;
import com.gonku.pos_be.dto.product.ProductMapper;
import com.gonku.pos_be.dto.product.ProductRequestDto;
import com.gonku.pos_be.dto.product.ProductResponseDto;
import com.gonku.pos_be.dto.product.ProductUpdateRequestDto;
import com.gonku.pos_be.entity.category.Category;
import com.gonku.pos_be.entity.product.Product;
import com.gonku.pos_be.entity.product.Uom;
import com.gonku.pos_be.exception.ResourceNotFoundException;
import com.gonku.pos_be.repository.ProductRepository;
import com.gonku.pos_be.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductValidationService productValidationService;
    private static final String ENTITY_NOT_FOUND = "Product";

    public PageResponseDto<ProductResponseDto> findAll(PageRequestDto pageRequestDto) {
        Pageable pageable = PageUtil.createPageable(pageRequestDto);
        Page<Product> productPage = productRepository.findAllWithPagination(
                pageRequestDto.getSearch(),
                pageRequestDto.getIsActive(),
                pageable);
        List<ProductResponseDto> responseDtoList = productPage.stream()
                .map(productMapper::toDto)
                .toList();

        return PageUtil.createPageResponse(responseDtoList, pageable, productPage.getTotalElements());
    }

    public ProductResponseDto findById(Long id) {
        return productMapper.toDto(productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND))));
    }

    public ProductResponseDto add(ProductRequestDto requestDto) {
        productValidationService
                .validateProductUniqueness(
                        requestDto.getBarcode(),
                        null
                );

        Product product = productMapper.toEntity(requestDto);
        product.setIsActive(true);

        Category category = productValidationService.validateCategoryExistence(requestDto.getCategoryId());
        product.setCategory(category);

        Uom uom = productValidationService.validateUomExistence(requestDto.getUomId());
        product.setUom(uom);

        return productMapper.toDto(productRepository.save(product));
    }

    public ProductResponseDto edit(Long id, ProductUpdateRequestDto requestDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND)));

        productValidationService
                .validateProductUniqueness(
                        requestDto.getBarcode(),
                        id
                );

        product = productMapper.updateFromDto(requestDto, product);
        product.setIsActive(true);

        Category category = productValidationService.validateCategoryExistence(requestDto.getCategoryId());
        product.setCategory(category);

        Uom uom = productValidationService.validateUomExistence(requestDto.getUomId());
        product.setUom(uom);

        return productMapper.toDto(productRepository.save(product));
    }

    public void remove(Long id) {
        Product category = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND)));

        category.setIsActive(false);

        productRepository.save(category);
    }
}
