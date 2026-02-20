package com.gonku.pos_be.dto.uom;

import com.gonku.pos_be.entity.product.Uom;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UomMapper {
    UomResponseDto toDto(Uom uom);

    List<UomResponseDto> toDtoList(List<Uom> uoms);

    @Mapping(target = "id", ignore = true)
    Uom toEntity(UomRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateFromDto(UomUpdateRequestDto requestDto, @MappingTarget Uom uom);
}
