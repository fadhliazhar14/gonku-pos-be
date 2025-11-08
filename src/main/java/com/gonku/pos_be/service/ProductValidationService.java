package com.gonku.pos_be.service;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.entity.Category;
import com.gonku.pos_be.entity.Uom;
import com.gonku.pos_be.exception.BusinessValidationException;
import com.gonku.pos_be.exception.ResourceNotFoundException;
import com.gonku.pos_be.repository.CategoryRepository;
import com.gonku.pos_be.repository.ProductRepository;
import com.gonku.pos_be.repository.UomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductValidationService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final UomRepository uomRepository;

    public void validateProductUniqueness(String name, Long id) {
        if (id == null) {
            if (productRepository.existsByBarcode(name)) {
                throw new BusinessValidationException(ResponseMessages.taken("Barcode"));
            }
        } else {
            if (productRepository.existsByBarcodeAndIdNot(name, id)) {
                throw new BusinessValidationException(ResponseMessages.taken("Barcode"));
            }
        }
    }

    public Category validateCategoryExistence(Long categoryId) {
        if (categoryId != null && categoryId > 0L) {
            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound("Category")));
        }

        return null;
    }

    public Uom validateUomExistence(Long uomId) {
        if (uomId != null && uomId > 0L) {
            return uomRepository.findById(uomId)
                    .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound("UoM")));
        }

        return null;
    }
}
