package com.generation.techshare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.techshare.dto.EquipmentRequestDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.mapper.EquipmentRequestMapper;
import com.generation.techshare.model.Equipment;
import com.generation.techshare.model.EquipmentRequest;
import com.generation.techshare.model.User;
import com.generation.techshare.repository.EquipmentRepository;
import com.generation.techshare.repository.EquipmentRequestRepository;
import com.generation.techshare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentRequestService {

    private final EquipmentRequestRepository equipmentRequestRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentRequestMapper equipmentRequestMapper;

    // Se l'utente è ADMIN restituisce tutte le richieste del DB, altrimenti solo le sue
    public List<EquipmentRequestDto> findAllForCurrentUser(String currentUserEmail) throws ServiceException {
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new ServiceException("Utente corrente non trovato");
        }

        // Se l'utente è ADMIN, carica tutte le richieste
        if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
            List<EquipmentRequest> allRequests = equipmentRequestRepository.findAll();
            return equipmentRequestMapper.toDtos(allRequests);
        }

        // Se è un utente normale, carica solo le sue
        List<EquipmentRequest> userRequests = equipmentRequestRepository.findByUserEmail(currentUserEmail);
        return equipmentRequestMapper.toDtos(userRequests);
    }

    // Trova una richiesta per ID (generale)
    public EquipmentRequestDto findById(Integer id) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata con ID: " + id));
        return equipmentRequestMapper.toDto(equipmentRequest);
    }

    // Trova una richiesta per ID verificando che l'utente sia il proprietario o ADMIN
    public EquipmentRequestDto findByIdWithPermission(Integer id, String currentUserEmail) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata con ID: " + id));

        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new ServiceException("Utente corrente non trovato");
        }

        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());
        boolean isOwner = equipmentRequest.getUser() != null && equipmentRequest.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ServiceException("Non hai i permessi per visualizzare questa richiesta");
        }

        return equipmentRequestMapper.toDto(equipmentRequest);
    }

    // Trova tutte le richieste (Senza filtri)
    public List<EquipmentRequestDto> findAll() {
        List<EquipmentRequest> equipmentRequests = equipmentRequestRepository.findAll();
        return equipmentRequestMapper.toDtos(equipmentRequests);
    }

    // Trova solo le richieste dell'utente specificato
    public List<EquipmentRequestDto> findByCurrentUser(String email) {
        List<EquipmentRequest> requests = equipmentRequestRepository.findByUserEmail(email);
        return equipmentRequestMapper.toDtos(requests);
    }

    // Crea una nuova richiesta
    public EquipmentRequestDto insertWithPermission(EquipmentRequestDto equipmentRequestDto, String currentUserEmail) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestMapper.toEntity(equipmentRequestDto);

        User user = userRepository.findByEmail(currentUserEmail);
        if (user == null) {
            throw new ServiceException("Utente corrente non trovato");
        }
        equipmentRequest.setUser(user);
        
        if (equipmentRequestDto.getEquipmentId() != null) {
            Equipment equipment = equipmentRepository.findById(equipmentRequestDto.getEquipmentId())
                    .orElseThrow(() -> new ServiceException("Attrezzatura non trovata con ID: " + equipmentRequestDto.getEquipmentId()));
            equipmentRequest.setEquipment(equipment);
        }
        
        EquipmentRequest savedEquipmentRequest = equipmentRequestRepository.save(equipmentRequest);
        return equipmentRequestMapper.toDto(savedEquipmentRequest);
    }

    // Aggiorna una richiesta
    public EquipmentRequestDto updateWithPermission(Integer id, EquipmentRequestDto equipmentRequestDto, String currentUserEmail) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata con ID: " + id));
        
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new ServiceException("Utente corrente non trovato");
        }
        
        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());
        boolean isOwner = equipmentRequest.getUser() != null && equipmentRequest.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ServiceException("Non hai permessi per modificare questa richiesta");
        }

        equipmentRequest.setTitle(equipmentRequestDto.getTitle());
        equipmentRequest.setDescription(equipmentRequestDto.getDescription());
        equipmentRequest.setLatitude(equipmentRequestDto.getLatitude());
        equipmentRequest.setLongitude(equipmentRequestDto.getLongitude());
        equipmentRequest.setSearchRadius(equipmentRequestDto.getSearchRadius());
        equipmentRequest.setStatus(equipmentRequestDto.getStatus());
        
        if (equipmentRequestDto.getEquipmentId() != null) {
            Equipment equipment = equipmentRepository.findById(equipmentRequestDto.getEquipmentId())
                    .orElseThrow(() -> new ServiceException("Attrezzatura non trovata con ID: " + equipmentRequestDto.getEquipmentId()));
            equipmentRequest.setEquipment(equipment);
        }
        
        EquipmentRequest updatedEquipmentRequest = equipmentRequestRepository.save(equipmentRequest);
        return equipmentRequestMapper.toDto(updatedEquipmentRequest);
    }

    // Elimina una richiesta
    public void deleteWithPermission(Integer id, String currentUserEmail) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata con ID: " + id));
        
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new ServiceException("Utente corrente non trovato");
        }
        
        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());
        boolean isOwner = equipmentRequest.getUser() != null && equipmentRequest.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ServiceException("Non hai permessi per eliminare questa richiesta");
        }
        
        equipmentRequestRepository.delete(equipmentRequest);
    }
}