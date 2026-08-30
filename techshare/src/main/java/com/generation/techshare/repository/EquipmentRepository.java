package com.generation.techshare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.generation.techshare.model.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    // Trova le attrezzature disponibili filtrate per categoria ed entro un determinato raggio (in km)
    @Query(value = "SELECT *, " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " +
            "cos(radians(longitude) - radians(:lng)) + sin(radians(:lat)) * " +
            "sin(radians(latitude)))) AS distance " +
            "FROM equipment " +
            "WHERE category_id = :categoryId AND available = 1 " +
            "HAVING distance < :radius " +
            "ORDER BY distance ASC", nativeQuery = true)
    List<Equipment> findMatchingEquipment(
            @Param("lat") Double lat, 
            @Param("lng") Double lng, 
            @Param("radius") Double radiusInKm,
            @Param("categoryId") Integer categoryId
    );
}