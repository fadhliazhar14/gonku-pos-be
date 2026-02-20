package com.gonku.pos_be.dto.product;

import com.gonku.pos_be.entity.product.Product;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "uom.id", target = "uomId")
    ProductResponseDto toDto(Product product);

    List<ProductResponseDto> toDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "uom", ignore = true)
    Product toEntity(ProductRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "uom", ignore = true)
    Product updateFromDto(ProductUpdateRequestDto requestDto, @MappingTarget Product product);
}
