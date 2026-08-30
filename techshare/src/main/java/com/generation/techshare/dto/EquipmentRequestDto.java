package com.generation.techshare.dto;

import lombok.Data;

@Data
public class EquipmentRequestDto {

    private Integer id;
    private String title;
    private String description;
    private Double latitude;
    private Double longitude;
    private Integer searchRadius;
    private String status;
    private Integer userId;
    private Integer categoryId;

    // Campi aggiuntivi per visualizzare i dettagli nel frontend
    private String ownerName;
    private String ownerPhone;
    private String locationName;
    private String categoryName;

}