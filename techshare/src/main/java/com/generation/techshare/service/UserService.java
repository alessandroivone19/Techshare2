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

    // Trova un utente per ID
    public UserDto findById(Integer id) throws ServiceException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + id));
        return userMapper.toDTO(user);
    }

    // Trova un utente per email
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
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
            // Hasha la password prima di salvarla
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            // Imposta il ruolo di default a USER se non specificato
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("USER");
            }
            User savedUser = userRepository.save(user);
            return userMapper.toDTO(savedUser);
        }catch(Exception e){
            throw new ServiceException("non è stato possibile inserire l'utente");
        }
    }

    // Aggiorna un utente (con controllo permessi)
    public UserDto updateWithPermission(Integer id, UserDto userDto, String currentUserEmail) throws ServiceException {
        User currentUser = findByEmail(currentUserEmail);
        
        // Controlla se l'utente corrente è ADMIN o sta modificando se stesso
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
        
        // Solo ADMIN può cambiare il ruolo
        if (currentUser.getRole().equals("ADMIN") && userDto.getRole() != null) {
            user.setRole(userDto.getRole());
        }
        
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }

    // Elimina un utente (con controllo permessi)
    public void deleteWithPermission(Integer id, String currentUserEmail) throws ServiceException {
        User currentUser = findByEmail(currentUserEmail);
        
        // Controlla se l'utente corrente è ADMIN o sta eliminando se stesso
        if (!currentUser.getRole().equals("ADMIN") && !currentUser.getId().equals(id)) {
            throw new ServiceException("Non hai permessi per eliminare questo utente");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ServiceException("Utente non trovato con ID: " + id));
        userRepository.delete(user);
    }

    // 👈 NUOVO METODO AGGIUNTO PER IL CAMBIO PASSWORD
    public void changePassword(String email, String currentPassword, String newPassword) throws ServiceException {
        // 1. Recupera l'utente tramite l'email estratta dal token JWT
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new ServiceException("Utente non trovato");
        }

        // 2. Verifica che la password attuale sia corretta
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new ServiceException("La password attuale non è corretta");
        }

        // 3. Cripta la nuova password e salvala
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}