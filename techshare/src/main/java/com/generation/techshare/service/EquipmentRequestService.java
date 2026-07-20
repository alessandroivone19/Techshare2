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

    // Crea una nuova richiesta
    public EquipmentRequestDto insert(EquipmentRequestDto equipmentRequestDto) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestMapper.toEntity(equipmentRequestDto);

        try {

            // Imposta user
            if (equipmentRequestDto.getUserId() != null) {
                User user = userRepository.findById(equipmentRequestDto.getUserId())
                        .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + equipmentRequestDto.getUserId()));
                equipmentRequest.setUser(user);
            }
            
            // Imposta equipment
            if (equipmentRequestDto.getEquipmentId() != null) {
                Equipment equipment = equipmentRepository.findById(equipmentRequestDto.getEquipmentId())
                        .orElseThrow(() -> new ServiceException("Attrezzatura non trovata con ID: " + equipmentRequestDto.getEquipmentId()));
                equipmentRequest.setEquipment(equipment);
            }
            
            // Imposta category
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

    // Aggiorna una richiesta
    public EquipmentRequestDto update(Integer id, EquipmentRequestDto equipmentRequestDto) throws ServiceException {
        try{
            EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(id)
                    .orElseThrow(() -> new ServiceException("Richiesta non trovata con ID: " + id));
    
            equipmentRequest.setTitle(equipmentRequestDto.getTitle());
            equipmentRequest.setDescription(equipmentRequestDto.getDescription());
            equipmentRequest.setLatitude(equipmentRequestDto.getLatitude());
            equipmentRequest.setLongitude(equipmentRequestDto.getLongitude());
            equipmentRequest.setSearchRadius(equipmentRequestDto.getSearchRadius());
            equipmentRequest.setStatus(equipmentRequestDto.getStatus());
            
            // Aggiorna user se fornito
            if (equipmentRequestDto.getUserId() != null) {
                User user = userRepository.findById(equipmentRequestDto.getUserId())
                        .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + equipmentRequestDto.getUserId()));
                equipmentRequest.setUser(user);
            }
            
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
            throw new ServiceException("non e stato possibile aggiornare la richiesta");
        }
    }

    // Elimina una richiesta
    public void delete(Integer id) throws ServiceException {
        EquipmentRequest equipmentRequest = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata con ID: " + id));
        equipmentRequestRepository.delete(equipmentRequest);
    }

}