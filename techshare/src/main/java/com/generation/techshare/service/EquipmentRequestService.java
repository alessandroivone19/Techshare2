package com.generation.techshare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.techshare.dto.EquipmentRequestDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.mapper.EquipmentRequestMapper;
import com.generation.techshare.model.Category;
import com.generation.techshare.model.EquipmentRequest;
import com.generation.techshare.model.User;
import com.generation.techshare.repository.CategoryRepository;
import com.generation.techshare.repository.EquipmentRequestRepository;
import com.generation.techshare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentRequestService {

    private final EquipmentRequestRepository equipmentRequestRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EquipmentRequestMapper equipmentRequestMapper;

    // Restituisce le richieste dell'utente corrente
    public List<EquipmentRequestDto> findAllForCurrentUser(String email) throws ServiceException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ServiceException("Utente non trovato");
        }
        List<EquipmentRequest> requests = equipmentRequestRepository.findByUserId(user.getId());
        return equipmentRequestMapper.toDtos(requests);
    }

    // Restituisce le richieste degli altri utenti
    public List<EquipmentRequestDto> findRequestsFromOtherUsers(String currentUserEmail) throws ServiceException {
        User currentUser = userRepository.findByEmail(currentUserEmail);
        if (currentUser == null) {
            throw new ServiceException("Utente non trovato");
        }
        
        List<EquipmentRequest> allRequests = equipmentRequestRepository.findAll();
        
        List<EquipmentRequest> otherRequests = allRequests.stream()
                .filter(req -> req.getUser() != null && !req.getUser().getEmail().equals(currentUserEmail))
                .toList();
                
        return equipmentRequestMapper.toDtos(otherRequests);
    }

    public EquipmentRequestDto findByIdWithPermission(Integer id, String currentUserEmail) throws ServiceException {
        EquipmentRequest request = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata"));
        return equipmentRequestMapper.toDto(request);
    }

    public EquipmentRequestDto insertWithPermission(EquipmentRequestDto requestDto, String currentUserEmail) throws ServiceException {
        User user = userRepository.findByEmail(currentUserEmail);
        if (user == null) {
            throw new ServiceException("Utente non trovato");
        }

        EquipmentRequest request = equipmentRequestMapper.toEntity(requestDto);
        request.setUser(user);

        // --- GESTIONE CATEGORIA CORRETTA ---
        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new ServiceException("Categoria non trovata con ID: " + requestDto.getCategoryId()));
            request.setCategory(category);
        } else {
            throw new ServiceException("La richiesta deve avere una categoria associata");
        }
        // -----------------------------------

        EquipmentRequest saved = equipmentRequestRepository.save(request);
        return equipmentRequestMapper.toDto(saved);
    }

    public EquipmentRequestDto updateWithPermission(Integer id, EquipmentRequestDto requestDto, String currentUserEmail) throws ServiceException {
        EquipmentRequest request = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata"));
        
        request.setDescription(requestDto.getDescription());
        
        EquipmentRequest updated = equipmentRequestRepository.save(request);
        return equipmentRequestMapper.toDto(updated);
    }

    public void deleteWithPermission(Integer id, String currentUserEmail) throws ServiceException {
        EquipmentRequest request = equipmentRequestRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Richiesta non trovata"));
        equipmentRequestRepository.delete(request);
    }
}