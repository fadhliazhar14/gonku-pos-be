package com.gonku.pos_be.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    private String barcode;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String name;

    @Size(max = 50, message = "Description max 50 characters")
    private String description;

    private BigDecimal costPrice;

    private BigDecimal salePrice;

    private Integer stock;

    private String uom;

    private String picturePath;

    private Boolean isActive;
}
