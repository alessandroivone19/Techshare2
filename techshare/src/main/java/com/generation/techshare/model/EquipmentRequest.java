package com.generation.techshare.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class EquipmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private Double latitude;

    private Double longitude;

    private Integer searchRadius;

    private String status;

    // Molte richieste possono appartenere ad una sola categoria
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Molte richieste possono appartenere ad una sola attrezzatura
    @ManyToOne
    @JoinColumn(name = "equipment_id")
    private Equipment equipment;

    // Molte richieste possono essere fatte da 1 utente
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}