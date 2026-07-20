package com.generation.techshare.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @GetMapping
    public ResponseEntity<List<EquipmentDto>> getAllEquipments() {
        return ResponseEntity.ok(equipmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEquipmentById(@PathVariable Integer id) {
        try {
            EquipmentDto equipmentDto = equipmentService.findById(id);
            return ResponseEntity.ok(equipmentDto);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createEquipment(@RequestBody EquipmentDto equipmentDto) {
        try {
            EquipmentDto createdEquipment = equipmentService.insert(equipmentDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEquipment);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateEquipment(@PathVariable Integer id, @RequestBody EquipmentDto equipmentDto) {
        try {
            EquipmentDto updatedEquipment = equipmentService.update(id, equipmentDto);
            return ResponseEntity.ok(updatedEquipment);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEquipment(@PathVariable Integer id) {
        try {
            equipmentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}