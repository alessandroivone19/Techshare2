package com.generation.techshare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.techshare.dto.EquipmentDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.mapper.EquipmentMapper;
import com.generation.techshare.model.Category;
import com.generation.techshare.model.Equipment;
import com.generation.techshare.model.User;
import com.generation.techshare.repository.CategoryRepository;
import com.generation.techshare.repository.EquipmentRepository;
import com.generation.techshare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EquipmentMapper equipmentMapper;

    // Trova un'attrezzatura per ID
    public EquipmentDto findById(Integer id) throws ServiceException {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Attrezzatura non trovata con ID: " + id));
        return equipmentMapper.toDTO(equipment);
    }

    // Trova tutte le attrezzature
    public List<EquipmentDto> findAll() {
        List<Equipment> equipments = equipmentRepository.findAll();
        return equipmentMapper.toDtos(equipments);
    }

    // Crea una nuova attrezzatura
    public EquipmentDto insert(EquipmentDto equipmentDto) throws ServiceException {
        Equipment equipment = equipmentMapper.toEntity(equipmentDto);
        try {
            // Imposta owner
            if (equipmentDto.getUserId() != null) {
                User owner = userRepository.findById(equipmentDto.getUserId())
                        .orElseThrow(() -> new ServiceException("Utente proprietario non trovato con ID: " + equipmentDto.getUserId()));
    
                equipment.setOwner(owner);
            }
            
            // Imposta category
            if (equipmentDto.getCategoryId() != null) {
                Category category = categoryRepository.findById(equipmentDto.getCategoryId())
                        .orElseThrow(() -> new ServiceException("Categoria non trovata con ID: " + equipmentDto.getCategoryId()));
                equipment.setCategory(category);
            }
            
            Equipment savedEquipment = equipmentRepository.save(equipment);
            return equipmentMapper.toDTO(savedEquipment);
            
        } catch (Exception e) {
            throw new ServiceException("non è stato possibile salvare l'equipaggiamento");
        }
    }

    // Aggiorna un'attrezzatura
    public EquipmentDto update(Integer id, EquipmentDto equipmentDto) throws ServiceException {
        try {
            Equipment equipment = equipmentRepository.findById(id)
                    .orElseThrow(() -> new ServiceException("Attrezzatura non trovata con ID: " + id));
    
            equipment.setTitle(equipmentDto.getTitle());
            equipment.setDescription(equipmentDto.getDescription());
            equipment.setImage(equipmentDto.getImage());
            equipment.setPossibleUses(equipmentDto.getPossibleUses());
            equipment.setAvailable(equipmentDto.getAvailable());
            equipment.setLatitude(equipmentDto.getLatitude());
            equipment.setLongitude(equipmentDto.getLongitude());
            
            // Aggiorna owner se fornito
            if (equipmentDto.getUserId() != null) {
                User owner = userRepository.findById(equipmentDto.getUserId())
                        .orElseThrow(() -> new ServiceException("Utente proprietario non trovato con ID: " + equipmentDto.getUserId()));
                equipment.setOwner(owner);
            }
            
            // Aggiorna category se fornita
            if (equipmentDto.getCategoryId() != null) {
                Category category = categoryRepository.findById(equipmentDto.getCategoryId())
                        .orElseThrow(() -> new ServiceException("Categoria non trovata con ID: " + equipmentDto.getCategoryId()));
                equipment.setCategory(category);
            }
            
            Equipment updatedEquipment = equipmentRepository.save(equipment);
            return equipmentMapper.toDTO(updatedEquipment);
            
        } catch (Exception e) {
            throw new ServiceException("non è stato possibile aggiornare l'equipaggiamento");
        }
    }

    // Elimina un'attrezzatura
    public void delete(Integer id) throws ServiceException {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Attrezzatura non trovata con ID: " + id));
        equipmentRepository.delete(equipment);
    }

}