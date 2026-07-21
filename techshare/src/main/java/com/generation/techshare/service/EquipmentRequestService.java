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

    // Trova una richiesta per ID (senza controllo permessi)
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

        // Verifica che l'utente loggato sia il creatore della richiesta o un ADMIN
        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());
        boolean isOwner = equipmentRequest.getUser() != null && equipmentRequest.getUser().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new ServiceException("Non hai i permessi per visualizzare questa richiesta");
        }

        return equipmentRequestMapper.toDto(equipmentRequest);
    }

    // Trova tutte le richieste (Generale)
    public List<EquipmentRequestDto> findAll() {
        List<EquipmentRequest> equipmentRequests = equipmentRequestRepository.findAll();
        return equipmentRequestMapper.toDtos(equipmentRequests);
    }

    // Trova solo le richieste dell'utente attualmente loggato
    public List<EquipmentRequestDto> findByCurrentUser(String email) {
        List<EquipmentRequest> requests = equipmentRequestRepository.findByUserEmail(email);
        return equipmentRequestMapper.toDtos(requests);
    }

    // Crea una nuova richiesta (con controllo permessi)
    public EquipmentRequestDto insertWithPermission(EquipmentRequestDto equipmentRequestDto, String currentUserEmail) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestMapper.toEntity(equipmentRequestDto);

        User user = userRepository.findByEmail(currentUserEmail);
        if (user == null) {
            throw new ServiceException("Utente corrente non trovato");
        }
        equipmentRequest.setUser(user);
        
        // Imposta equipment se fornito
        if (equipmentRequestDto.getEquipmentId() != null) {
            Equipment equipment = equipmentRepository.findById(equipmentRequestDto.getEquipmentId())
                    .orElseThrow(() -> new ServiceException("Attrezzatura non trovata con ID: " + equipmentRequestDto.getEquipmentId()));
            equipmentRequest.setEquipment(equipment);
        }
        
        EquipmentRequest savedEquipmentRequest = equipmentRequestRepository.save(equipmentRequest);
        return equipmentRequestMapper.toDto(savedEquipmentRequest);
    }

    // Aggiorna una richiesta (con controllo permessi)
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

    // Elimina una richiesta (con controllo permessi)
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