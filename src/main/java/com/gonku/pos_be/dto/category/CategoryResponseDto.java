package com.gonku.pos_be.dto.category;

import lombok.Data;

@Data
public class CategoryResponseDto {
    private Long id;
    private String name;
    private String description;
    private String picturePath;
    private Boolean isActive;
}
