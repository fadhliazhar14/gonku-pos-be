package com.gonku.pos_be.dto.uom;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UomRequestDto {
    @NotBlank(message = "UoM code is required")
    @Size(min = 3, max = 20, message = "UoM code must be between 3 and 20 characters")
    private String code;

    @NotBlank(message = "UoM name is required")
    @Size(min = 3, max = 50, message = "UoM name must be between 3 and 50 characters")
    private String name;

    @Size(max = 50, message = "Description max 50 characters")
    private String description;

    @NotBlank(message = "UoM symbol is required")
    @Size(min = 3, max = 10, message = "UoM symbol must be between 3 and 10 characters")
    private String symbol;

    private Boolean isActive;
}
