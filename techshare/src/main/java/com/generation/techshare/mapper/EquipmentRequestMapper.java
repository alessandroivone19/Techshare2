package com.generation.techshare.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.generation.techshare.dto.EquipmentRequestDto;
import com.generation.techshare.model.EquipmentRequest;

@Mapper(componentModel = "spring")
public interface EquipmentRequestMapper {

    // EquipmentRequest Entity -> EquipmentRequestDTO
   @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "ownerName", expression = "java(equipmentRequest.getUser() != null ? equipmentRequest.getUser().getFirstName() + \" \" + equipmentRequest.getUser().getLastName() : null)")
    @Mapping(source = "user.phoneNumber", target = "ownerPhone")
    @Mapping(source = "user.city", target = "locationName") // Prende la città vera dell'utente dal DB
    @Mapping(source = "latitude", target = "latitude")
    @Mapping(source = "longitude", target = "longitude")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    EquipmentRequestDto toDto(EquipmentRequest equipmentRequest);

    // EquipmentRequestDTO -> EquipmentRequest Entity
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "category", ignore = true)
    EquipmentRequest toEntity(EquipmentRequestDto equipmentRequestDto);

    List<EquipmentRequestDto> toDtos(List<EquipmentRequest> equipmentRequests);

    List<EquipmentRequest> toEntity(List<EquipmentRequestDto> dtos);

}