package com.gonku.pos_be.service;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.exception.BusinessValidationException;
import com.gonku.pos_be.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class CategoryValidationService {
    private final CategoryRepository categoryRepository;

    public void validateCategoryUniqueness(String name, Long id) {
        if (id == null) {
            if (categoryRepository.existsByName(name)) {
                throw new BusinessValidationException(ResponseMessages.taken("Name"));
            }
        } else {
            if (categoryRepository.existsByNameAndIdNot(name, id)) {
                throw new BusinessValidationException(ResponseMessages.taken("Name"));
            }
        }
    }
}
