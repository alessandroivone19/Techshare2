package com.generation.techshare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.generation.techshare.model.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    // Trova le attrezzature entro un determinato raggio (in km) partendo da latitudine e longitudine
    @Query(value = "SELECT *, " +
           "(6371 * acos(cos(radians(:lat)) * cos(radians(latitude)) * " +
           "cos(radians(longitude) - radians(:lng)) + sin(radians(:lat)) * " +
           "sin(radians(latitude)))) AS distance " +
           "FROM equipment " +
           "HAVING distance < :radius " +
           "ORDER BY distance ASC", nativeQuery = true)
    List<Equipment> findByLocationNear(@Param("lat") Double lat, 
                                       @Param("lng") Double lng, 
                                       @Param("radius") Double radiusInKm);
}