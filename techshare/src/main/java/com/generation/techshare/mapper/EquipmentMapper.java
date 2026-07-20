package com.generation.techshare.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.techshare.dto.EquipmentDto;
import com.generation.techshare.model.Equipment;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {

    // Equipment Entity -> EquipmentDTO
    @Mapping(source = "owner.id", target = "userId")
    @Mapping(source = "category.id", target = "categoryId")
    EquipmentDto toDTO(Equipment equipment);

    // EquipmentDTO -> Equipment Entity
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "category", ignore = true)
    Equipment toEntity(EquipmentDto equipmentDto);

    List<EquipmentDto> toDtos (List<Equipment> equipments);
    List<Equipment> toEntity (List<EquipmentDto> dtos);
   

}