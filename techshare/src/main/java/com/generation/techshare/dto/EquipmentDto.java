package com.generation.techshare.dto;

import lombok.Data;


@Data
public class EquipmentDto {

    private Integer id;
    private String title;
    private String description;
    private String image;
    private String possibleUses;
    private Boolean available;
    private Double latitude;
    private Double longitude;
    private Integer userId;
    private Integer categoryId;

}