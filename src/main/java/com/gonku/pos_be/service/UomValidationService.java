package com.gonku.pos_be.service;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.exception.BusinessValidationException;
import com.gonku.pos_be.repository.UomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UomValidationService {
    private final UomRepository uomRepository;

    public void validateUomUniqueness(String name, Long id) {
        if (id == null) {
            if (uomRepository.existsByName(name)) {
                throw new BusinessValidationException(ResponseMessages.taken("Name"));
            }
        } else {
            if (uomRepository.existsByNameAndIdNot(name, id)) {
                throw new BusinessValidationException(ResponseMessages.taken("Name"));
            }
        }
    }
}
