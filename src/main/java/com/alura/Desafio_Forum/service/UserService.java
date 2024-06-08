package com.alura.Desafio_Forum.service;

import com.alura.Desafio_Forum.domain.User;
import com.alura.Desafio_Forum.dto.UserDTO;
import com.alura.Desafio_Forum.dto.UserIdLoginDTO;
import com.alura.Desafio_Forum.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long saveUser(UserDTO userDTO) {
        // Validate DTO fields
        if (userDTO.login() == null || userDTO.senha() == null) {
            throw new IllegalArgumentException("Login and senha fields are required.");
        }

        // Check if user already exists
        if (userRepository.existsByLogin(userDTO.login())) {
            throw new IllegalStateException("User with this login already exists.");
        }

        // Create User entity from DTO
        User user = new User();
        user.setLogin(userDTO.login());
        // Encode password before saving
        user.setSenha(passwordEncoder.encode(userDTO.senha()));
        // Set status to true for new registrations
        user.setStatus(true);

        // Save user to repository
        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public Page<UserIdLoginDTO> getAllUsers(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(user -> new UserIdLoginDTO(user.getId(), user.getLogin(), user.isStatus()));
    }

    @Transactional
    public void updateUser(Long userId, UserIdLoginDTO detalhamentoUserDto) {
        // Check if user exists
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found with ID: " + userId);
        }

        // Get the existing user
        User existingUser = optionalUser.get();
        // Update user fields
        existingUser.setLogin(detalhamentoUserDto.login());
        existingUser.setStatus(detalhamentoUserDto.status());

        // Save the updated user
        userRepository.save(existingUser);
    }

    @Transactional
    public void deleteUser(Long userId) {
        // Check if user exists
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found with ID: " + userId);
        }

        // Get the existing user
        User existingUser = optionalUser.get();

        // Set status to false
        existingUser.setStatus(false);

        // Save the updated user
        userRepository.save(existingUser);
    }
}

