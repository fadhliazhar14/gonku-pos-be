package com.gonku.pos_be.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequestDto {
    @Size(min = 13, max = 13, message = "Product barcode must be 13 characters")
    private String barcode;

    @NotNull(message = "Product name is required")
    @NotBlank(message = "This field cannot be empty")
    @Size(min = 3, max = 250, message = "Product name must be between 3 and 250 characters")
    private String name;

    @Size(max = 50, message = "Description max 50 characters")
    private String description;

    @NotNull(message = "Buying price is required")
    @Min(value = 1, message = "Buying price must be at least 1")
    private BigDecimal buyingPrice;

    @NotNull(message = "Sale price is required")
    @Min(value = 1, message = "Sale price must be at least 1")
    private BigDecimal salePrice;

    @Min(value = 0, message = "Stock must be equal or greater than 0")
    private BigDecimal stock;

    private String picturePath;

    @Min(value = 0, message = "Category ID must be equal or greater than 0")
    private Long categoryId;

    @Min(value = 0, message = "UoM ID must be equal or greater than 0")
    private Long uomId;

    private Boolean isActive;
}
