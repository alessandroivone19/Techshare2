package com.generation.techshare.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.techshare.dto.EquipmentDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.service.EquipmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("techshare/api/equipments")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    // Tutti possono vedere tutte le attrezzature
    @GetMapping
    public ResponseEntity<List<EquipmentDto>> getAllEquipments() {
        return ResponseEntity.ok(equipmentService.findAll());
    }

    // Tutti possono vedere una singola attrezzatura
    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipmentById(@PathVariable Integer id) {
        try {
            EquipmentDto equipmentDto = equipmentService.findById(id);
            return ResponseEntity.ok(equipmentDto);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Solo autenticati possono creare attrezzature
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createEquipment(@RequestBody EquipmentDto equipmentDto) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            EquipmentDto createdEquipment = equipmentService.insertWithPermission(equipmentDto, currentUserEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEquipment);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Solo il proprietario o ADMIN possono modificare
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateEquipment(@PathVariable Integer id, @RequestBody EquipmentDto equipmentDto) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            EquipmentDto updatedEquipment = equipmentService.updateWithPermission(id, equipmentDto, currentUserEmail);
            return ResponseEntity.ok(updatedEquipment);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Solo il proprietario o ADMIN possono eliminare
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteEquipment(@PathVariable Integer id) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            equipmentService.deleteWithPermission(id, currentUserEmail);
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}