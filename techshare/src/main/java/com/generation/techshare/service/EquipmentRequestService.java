package com.generation.techshare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.techshare.dto.EquipmentRequestDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.mapper.EquipmentRequestMapper;
import com.generation.techshare.model.Category;
import com.generation.techshare.model.Equipment;
import com.generation.techshare.model.EquipmentRequest;
import com.generation.techshare.model.User;
import com.generation.techshare.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;
    private final EquipmentRequestMapper equipmentRequestMapper;

    // Trova una richiesta per ID
    public EquipmentRequestDto findById(Integer id) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata con ID: " + id));
        return equipmentRequestMapper.toDto(equipmentRequest);
    }

    // Trova tutte le richieste
    public List<EquipmentRequestDto> findAll() {
        List<EquipmentRequest> equipmentRequests = equipmentRequestRepository.findAll();
        return equipmentRequestMapper.toDtos(equipmentRequests);
    }

    // Crea una nuova richiesta (con controllo permessi)
    public EquipmentRequestDto insertWithPermission(EquipmentRequestDto equipmentRequestDto, String currentUserEmail) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestMapper.toEntity(equipmentRequestDto);

        try {
            // Imposta automaticamente l'utente come l'utente corrente
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
            
            // Imposta category se fornita
            if (equipmentRequestDto.getCategoryId() != null) {
                Category category = categoryRepository.findById(equipmentRequestDto.getCategoryId())
                        .orElseThrow(() -> new ServiceException("Categoria non trovata con ID: " + equipmentRequestDto.getCategoryId()));
                equipmentRequest.setCategory(category);
            }
            
            EquipmentRequest savedEquipmentRequest = equipmentRequestRepository.save(equipmentRequest);
            return equipmentRequestMapper.toDto(savedEquipmentRequest);
            
        } catch (Exception e) {
            throw new ServiceException("non è stato possibile inserire la richiesta");
        }
        
    }

    // Aggiorna una richiesta (con controllo permessi)
    public EquipmentRequestDto updateWithPermission(Integer id, EquipmentRequestDto equipmentRequestDto, String currentUserEmail) throws ServiceException {
        try{
            EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(id)
                    .orElseThrow(() -> new ServiceException("Richiesta non trovata con ID: " + id));
            
            // Controlla se l'utente corrente ha fatto la richiesta o è ADMIN
            User currentUser = userRepository.findByEmail(currentUserEmail);
            if (currentUser == null) {
                throw new ServiceException("Utente corrente non trovato");
            }
            
            if (!currentUser.getRole().equals("ADMIN") && !equipmentRequest.getUser().getId().equals(currentUser.getId())) {
                throw new ServiceException("Non hai permessi per modificare questa richiesta");
            }
    
            equipmentRequest.setTitle(equipmentRequestDto.getTitle());
            equipmentRequest.setDescription(equipmentRequestDto.getDescription());
            equipmentRequest.setLatitude(equipmentRequestDto.getLatitude());
            equipmentRequest.setLongitude(equipmentRequestDto.getLongitude());
            equipmentRequest.setSearchRadius(equipmentRequestDto.getSearchRadius());
            equipmentRequest.setStatus(equipmentRequestDto.getStatus());
            
            // Aggiorna equipment se fornito
            if (equipmentRequestDto.getEquipmentId() != null) {
                Equipment equipment = equipmentRepository.findById(equipmentRequestDto.getEquipmentId())
                        .orElseThrow(() -> new ServiceException("Attrezzatura non trovata con ID: " + equipmentRequestDto.getEquipmentId()));
                equipmentRequest.setEquipment(equipment);
            }
            
            // Aggiorna category se fornita
            if (equipmentRequestDto.getCategoryId() != null) {
                Category category = categoryRepository.findById(equipmentRequestDto.getCategoryId())
                        .orElseThrow(() -> new ServiceException("Categoria non trovata con ID: " + equipmentRequestDto.getCategoryId()));
                equipmentRequest.setCategory(category);
            }
            
            EquipmentRequest updatedEquipmentRequest = equipmentRequestRepository.save(equipmentRequest);
            return equipmentRequestMapper.toDto(updatedEquipmentRequest);
        }catch (Exception e){
            throw new ServiceException("non è stato possibile aggiornare la richiesta");
        }
    }

    // Elimina una richiesta (con controllo permessi)
    public void deleteWithPermission(Integer id, String currentUserEmail) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata con ID: " + id));
        
        // Controlla se l'utente corrente ha fatto la richiesta o è ADMIN
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new ServiceException("Utente corrente non trovato");
        }
        
        if (!currentUser.getRole().equals("ADMIN") && !equipmentRequest.getUser().getId().equals(currentUser.getId())) {
            throw new ServiceException("Non hai permessi per eliminare questa richiesta");
        }
        
        equipmentRequestRepository.delete(equipmentRequest);
    }

}