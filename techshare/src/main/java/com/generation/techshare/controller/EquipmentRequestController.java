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

import com.generation.techshare.dto.EquipmentRequestDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.service.EquipmentRequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("techshare/api/requests")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class EquipmentRequestController {

    private final EquipmentRequestService equipmentRequestService;

    // Se l'utente è ADMIN restituisce TUTTE le richieste, se è USER restituisce solo le SUE
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllRequests() {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            List<EquipmentRequestDto> requests = equipmentRequestService.findAllForCurrentUser(currentUserEmail);
            return ResponseEntity.ok(requests);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Dettaglio richiesta per ID (solo proprietario o ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRequestById(@PathVariable Integer id) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            EquipmentRequestDto requestDto = equipmentRequestService.findByIdWithPermission(id, currentUserEmail);
            return ResponseEntity.ok(requestDto);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Creazione nuova richiesta (assegnata in automatico all'utente loggato)
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createRequest(@RequestBody EquipmentRequestDto requestDto) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            EquipmentRequestDto createdRequest = equipmentRequestService.insertWithPermission(requestDto, currentUserEmail);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Modifica richiesta (solo proprietario o ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateRequest(@PathVariable Integer id, @RequestBody EquipmentRequestDto requestDto) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            EquipmentRequestDto updatedRequest = equipmentRequestService.updateWithPermission(id, requestDto, currentUserEmail);
            return ResponseEntity.ok(updatedRequest);
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Eliminazione richiesta (solo proprietario o ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteRequest(@PathVariable Integer id) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            equipmentRequestService.deleteWithPermission(id, currentUserEmail);
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}