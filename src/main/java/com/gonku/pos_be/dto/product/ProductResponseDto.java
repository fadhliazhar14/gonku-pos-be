package com.gonku.pos_be.dto.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponseDto {
    private Long id;
    private String barcode;
    private String name;
    private String description;
    private BigDecimal buyingPrice;
    private BigDecimal salePrice;
    private BigDecimal stock;
    private String picturePath;
    private Boolean isActive;
    private Long categoryId;
    private Long uomId;
}
