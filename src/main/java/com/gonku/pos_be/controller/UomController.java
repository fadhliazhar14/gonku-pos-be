package com.gonku.pos_be.controller;

import com.gonku.pos_be.constant.ResponseMessages;
import com.gonku.pos_be.dto.common.PageRequestDto;
import com.gonku.pos_be.dto.common.PageResponseDto;
import com.gonku.pos_be.dto.uom.UomRequestDto;
import com.gonku.pos_be.dto.uom.UomResponseDto;
import com.gonku.pos_be.dto.uom.UomUpdateRequestDto;
import com.gonku.pos_be.service.UomService;
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
public class UomController {
    private final UomService uomService;
    private static final String ENTITY_RESPONSE = "UoM";
    private static final String PATH_WITH_ID = "/uom/{id}";
    private static final String PATH_WITHOUT_ID = "/uom";

    @GetMapping(PATH_WITHOUT_ID)
    public ResponseEntity<ApiResponse<PageResponseDto<UomResponseDto>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(defaultValue = "true") Boolean isActive,
            @RequestParam(required = false) String search
    ) {
        PageRequestDto pageRequestDto = new PageRequestDto(page, size, sort, direction, isActive, search);
        PageResponseDto<UomResponseDto> uomList = uomService.findAll(pageRequestDto);
        ApiResponse<PageResponseDto<UomResponseDto>> response = ApiResponse.success(ResponseMessages.SUCCESS, uomList);

        return ResponseEntity.ok(response);
    }

    @GetMapping(PATH_WITH_ID)
    public ResponseEntity<ApiResponse<UomResponseDto>> getById(@PathVariable Long id) {
        UomResponseDto uom = uomService.findById(id);
        ApiResponse<UomResponseDto> response = ApiResponse.success(ResponseMessages.SUCCESS, uom);

        return ResponseEntity.ok(response);
    }

    @PostMapping(PATH_WITHOUT_ID)
    public ResponseEntity<ApiResponse<UomResponseDto>> create(@Valid @RequestBody UomRequestDto requestDto) {
        UomResponseDto createdUom = uomService.add(requestDto);
        ApiResponse<UomResponseDto> response = ApiResponse.success(
                HttpStatus.CREATED.value(), ResponseMessages.created(ENTITY_RESPONSE), createdUom);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUom.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping(PATH_WITH_ID)
    public ResponseEntity<ApiResponse<UomResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody UomUpdateRequestDto requestDto
    ) {
        UomResponseDto updatedUom = uomService.edit(id, requestDto);
        ApiResponse<UomResponseDto> response = ApiResponse.success(ResponseMessages.updated(ENTITY_RESPONSE), updatedUom);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(PATH_WITH_ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        uomService.remove(id);
        ApiResponse<Void> response = ApiResponse.success(ResponseMessages.deleted(ENTITY_RESPONSE), null);

        return ResponseEntity.ok(response);
    }
}
