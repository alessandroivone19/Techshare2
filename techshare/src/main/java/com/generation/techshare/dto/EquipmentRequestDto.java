package com.generation.techshare.dto;

import lombok.Data;


@Data
public class EquipmentRequestDto {

    private Integer id;
    private String title;
    // descrizione della richiesta es: cerco drone per foto matrimonio
    private String description;
    private Double latitude;
    private Double longitude;
    private Integer searchRadius;
    private String status;
    private Integer userId;
    private Integer equipmentId;
    private Integer categoryId;

}