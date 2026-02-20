package com.gonku.pos_be.service;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.dto.common.PageRequestDto;
import com.gonku.pos_be.dto.common.PageResponseDto;
import com.gonku.pos_be.dto.uom.UomMapper;
import com.gonku.pos_be.dto.uom.UomRequestDto;
import com.gonku.pos_be.dto.uom.UomResponseDto;
import com.gonku.pos_be.dto.uom.UomUpdateRequestDto;
import com.gonku.pos_be.entity.product.Uom;
import com.gonku.pos_be.exception.ResourceNotFoundException;
import com.gonku.pos_be.repository.UomRepository;
import com.gonku.pos_be.util.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UomService {
    private final UomRepository uomRepository;
    private final UomMapper uomMapper;
    private final UomValidationService uomValidationService;
    private static final String ENTITY_NOT_FOUND = "UoM";

    public PageResponseDto<UomResponseDto> findAll(PageRequestDto pageRequestDto) {
        Pageable pageable = PageUtil.createPageable(pageRequestDto);
        Page<Uom> uomPage = uomRepository.findAllWithPagination(
                pageRequestDto.getSearch(),
                pageRequestDto.getIsActive(),
                pageable);
        List<UomResponseDto> responseDtoList = uomPage.stream()
                .map(uomMapper::toDto)
                .toList();

        return PageUtil.createPageResponse(responseDtoList, pageable, uomPage.getTotalElements());
    }

    public UomResponseDto findById(Long id) {
        return uomMapper.toDto(uomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND))));
    }

    public UomResponseDto add(UomRequestDto requestDto) {
        uomValidationService
                .validateUomUniqueness(
                        requestDto.getName(),
                        null
                );

        requestDto.setIsActive(true);

        return uomMapper.toDto(uomRepository.save(uomMapper.toEntity(requestDto)));
    }

    public UomResponseDto edit(Long id, UomUpdateRequestDto requestDto) {
        Uom uom = uomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND)));

        uomValidationService
                .validateUomUniqueness(
                        requestDto.getName(),
                        id
                );


        uomMapper.updateFromDto(requestDto, uom);
        Uom updatedUom = uomRepository.save(uom);

        return uomMapper.toDto(updatedUom);
    }

    public void remove(Long id) {
        Uom uom = uomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResponseMessages.notFound(ENTITY_NOT_FOUND)));

        uom.setIsActive(false);

        uomRepository.save(uom);
    }
}
