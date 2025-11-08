package com.gonku.pos_be.dto.product;

import com.gonku.pos_be.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toDto(Product product);

    List<ProductResponseDto> toDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "uom", ignore = true)
    Product toEntity(ProductRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "uom", ignore = true)
    Product updateFromDto(ProductUpdateRequestDto requestDto, @MappingTarget Product product);
}
