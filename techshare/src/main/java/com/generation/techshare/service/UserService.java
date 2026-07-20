package com.generation.techshare.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.generation.techshare.dto.UserDto;
import com.generation.techshare.exception.ServiceException;
import com.generation.techshare.mapper.UserMapper;
import com.generation.techshare.model.User;
import com.generation.techshare.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // Trova un utente per ID
    public UserDto findById(Integer id) throws ServiceException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + id));
        return userMapper.toDTO(user);
    }

    // Trova tutti gli utenti
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtos(users);
    }

    // Crea un nuovo utente
    public UserDto insert(UserDto userDto) throws ServiceException {
        try{
            User user = userMapper.toEntity(userDto);
            User savedUser = userRepository.save(user);
            return userMapper.toDTO(savedUser);
        }catch(Exception e){
            throw new ServiceException("non è stato possibile inserire l'utente");
        }
    }

    // Aggiorna un utente
    public UserDto update(Integer id, UserDto userDto) throws ServiceException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + id));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());        // da rivedere
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setCity(userDto.getCity());
        
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    // Elimina un utente
    public void delete(Integer id) throws ServiceException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + id));
        userRepository.delete(user);
    }

}