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

    // Solo l'utente autenticato può vedere LE SUE richieste
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EquipmentRequestDto>> getAllRequests() {
        // Recuperiamo l'email dell'utente attualmente autenticato dal JWT
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        
        // Passiamo l'email al service per filtrare le sole richieste dell'utente
        List<EquipmentRequestDto> userRequests = equipmentRequestService.findByCurrentUser(currentUserEmail);
        
        return ResponseEntity.ok(userRequests);
    }

    // Un utente può vedere il dettaglio di una richiesta SOLO se gli appartiene (o se è ADMIN)
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getRequestById(@PathVariable Integer id) {
        try {
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            // Passiamo l'email per verificare i permessi di lettura
            EquipmentRequestDto requestDto = equipmentRequestService.findByIdWithPermission(id, currentUserEmail);
            return ResponseEntity.ok(requestDto);
        } catch (ServiceException e) {
            // Se l'utente non è autorizzato o la risorsa non esiste, restituiamo FORBIDDEN o NOT_FOUND
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    // Solo autenticati possono creare richieste
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

    // Solo chi ha fatto la richiesta o ADMIN possono modificare
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

    // Solo chi ha fatto la richiesta o ADMIN possono eliminare
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