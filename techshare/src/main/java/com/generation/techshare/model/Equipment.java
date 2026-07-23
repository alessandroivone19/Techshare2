package com.generation.techshare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String description;

    // Modifica per supportare immagini in Base64 di grandi dimensioni
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    private String possibleUses;
    private Boolean available;
    private Double latitude;
    private Double longitude;

    // Molte attrezzature possono appartenere a un solo utente
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    // Molte attrezzature possono appartenere a una sola categoria
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}