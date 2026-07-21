package com.generation.techshare.mapper;

import com.generation.techshare.dto.EquipmentRequestDto;
import com.generation.techshare.model.EquipmentRequest;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface EquipmentRequestMapper {

    // EquipmentRequest Entity -> EquipmentRequestDTO
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "equipment.id", target = "equipmentId")
    EquipmentRequestDto toDto(EquipmentRequest equipmentRequest);

    // EquipmentRequestDTO -> EquipmentRequest Entity
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "equipment", ignore = true)
    EquipmentRequest toEntity(EquipmentRequestDto equipmentRequestDto);

    List<EquipmentRequestDto> toDtos(List<EquipmentRequest> equipmentRequests);

    List<EquipmentRequest> toEntity(List<EquipmentRequestDto> dtos);

}