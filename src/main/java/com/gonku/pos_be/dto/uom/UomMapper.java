package com.gonku.pos_be.dto.uom;

import com.gonku.pos_be.entity.Uom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UomMapper {
    UomResponseDto toDto(Uom uom);

    List<UomResponseDto> toDtoList(List<Uom> uoms);

    @Mapping(target = "id", ignore = true)
    Uom toEntity(UomRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateFromDto(UomUpdateRequestDto requestDto, @MappingTarget Uom uom);
}
