package com.generation.techshare.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.techshare.model.EquipmentRequest;

public interface EquipmentRequestRepository extends JpaRepository<EquipmentRequest, Integer> {

    // Trova tutte le richieste fatte dall'utente filtrando per la sua email
    List<EquipmentRequest> findByUserEmail(String email);

    // Oppure trovale tramite l'ID dell'utente
    List<EquipmentRequest> findByUserId(Integer userId);
}