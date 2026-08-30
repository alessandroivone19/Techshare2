package com.generation.techshare.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public UserDto findById(Integer id) throws ServiceException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + id));
        return userMapper.toDTO(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDto findDtoByEmail(String email) throws ServiceException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ServiceException("Utente non trovato con email: " + email);
        }
        return userMapper.toDTO(user);
    }

    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return userMapper.toDtos(users);
    }

    public UserDto insert(UserDto userDto) throws ServiceException {
        if (userRepository.findByEmail(userDto.getEmail()) != null) {
            throw new ServiceException("Email già registrata");
        }
        
        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");
        }
        
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public UserDto updateWithPermission(Integer id, UserDto userDto, String currentUserEmail) throws ServiceException {
        User currentUser = findByEmail(currentUserEmail);
        
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(id)) {
            throw new ServiceException("Non hai permessi per modificare questo utente");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + id));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setCity(userDto.getCity());
        
        if (currentUser.getRole().equals("ADMIN") && userDto.getRole() != null) {
            user.setRole(userDto.getRole());
        }
        
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    public void deleteWithPermission(Integer id, String currentUserEmail) throws ServiceException {
        User currentUser = findByEmail(currentUserEmail);
        
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(id)) {
            throw new ServiceException("Non hai permessi per eliminare questo utente");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + id));
        userRepository.delete(user);
    }

    public void changePassword(String email, String currentPassword, String newPassword) throws ServiceException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ServiceException("Utente non trovato");
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ServiceException("La password attuale non è corretta");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}