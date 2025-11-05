package com.gonku.pos_be.dto.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageRequestDto {
    @Min(value = 0, message = "Page number must be non-negative")
    private int page;

    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 100, message = "Page size must not exceed 100")
    private int size;

    private String sort;

    private String direction;

    private Boolean isActive;

    private String search;
}
