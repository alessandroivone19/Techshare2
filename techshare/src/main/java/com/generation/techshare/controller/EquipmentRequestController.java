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

import com.generation.techshare.dto.EquipmentRequestDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.service.EquipmentRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class EquipmentRequestController {

    private final EquipmentRequestService equipmentRequestService;

    @GetMapping
    public ResponseEntity<List<EquipmentRequestDto>> getAllRequests() {
        return ResponseEntity.ok(equipmentRequestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRequestById(@PathVariable Integer id) {
        try {
            EquipmentRequestDto requestDto = equipmentRequestService.findById(id);
            return ResponseEntity.ok(requestDto);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createRequest(@RequestBody EquipmentRequestDto requestDto) {
        try {
            EquipmentRequestDto createdRequest = equipmentRequestService.insert(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable Integer id, @RequestBody EquipmentRequestDto requestDto) {
        try {
            EquipmentRequestDto updatedRequest = equipmentRequestService.update(id, requestDto);
            return ResponseEntity.ok(updatedRequest);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRequest(@PathVariable Integer id) {
        try {
            equipmentRequestService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}