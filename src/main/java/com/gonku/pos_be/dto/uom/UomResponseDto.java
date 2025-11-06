package com.gonku.pos_be.dto.uom;

import lombok.Data;

@Data
public class UomResponseDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private String symbol;
    private Boolean isActive;
}
