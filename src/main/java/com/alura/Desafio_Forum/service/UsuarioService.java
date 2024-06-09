package com.alura.Desafio_Forum.service;

import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.UsuarioDTO;
import com.alura.Desafio_Forum.dto.UsuarioIdEmailDto;
import com.alura.Desafio_Forum.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long saveUser(UsuarioDTO userDTO) {
        // Validate DTO fields
        if (userDTO.email() == null || userDTO.senha() == null) {
            throw new IllegalArgumentException("Login and senha fields are required.");
        }

        // Check if user already exists
        if (usuarioRepository.existsByEmail(userDTO.email())) {
            throw new IllegalStateException("User with this login already exists.");
        }

        // Create User entity from DTO
        Usuario user = new Usuario();
        user.setNome(userDTO.nome());
        user.setEmail(userDTO.email());
        // Encode password before saving
        user.setSenha(passwordEncoder.encode(userDTO.senha()));
        // Set status to true for new registrations
        user.setStatus(true);

        // Save user to repository
        Usuario savedUser = usuarioRepository.save(user);
        return savedUser.getId();
    }

    public Page<UsuarioIdEmailDto> getAllUsers(Pageable pageable) {
        Page<Usuario> usersPage = usuarioRepository.findAll(pageable);
        return usersPage.map(user -> new UsuarioIdEmailDto(user.getId(), user.getNome(), user.getEmail(), user.isStatus()));
    }

    @Transactional
    public void updateUser(Long userId, UsuarioIdEmailDto detalhamentoUserDto) {
        // Check if user exists
        Optional<Usuario> optionalUser = usuarioRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found with ID: " + userId);
        }

        // Get the existing user
        Usuario existingUser = optionalUser.get();

        // Update user fields
        existingUser.setNome(detalhamentoUserDto.nome());
        existingUser.setEmail(detalhamentoUserDto.email());

        // Safely handle status update using Optional methods
        existingUser.setStatus(detalhamentoUserDto.status() ? detalhamentoUserDto.status() : existingUser.isStatus());

        // Save the updated user
        usuarioRepository.save(existingUser);
    }


    @Transactional
    public void deleteUser(Long userId) {
        // Check if user exists
        Optional<Usuario> optionalUser = usuarioRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalStateException("User not found with ID: " + userId);
        }

        // Get the existing user
        Usuario existingUser = optionalUser.get();

        // Set status to false
        existingUser.setStatus(false);

        // Save the updated user
        usuarioRepository.save(existingUser);
    }
}

