package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.domain.User;
import com.alura.Desafio_Forum.dto.UserDTO;
import com.alura.Desafio_Forum.dto.UserIdLoginDTO;
import com.alura.Desafio_Forum.repository.UserRepository;
import com.alura.Desafio_Forum.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;


@RestController
@RequestMapping("/user")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserService userService;

    @PostMapping("")
    @Transactional
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid UserDTO userDTO,
                                                   UriComponentsBuilder uriComponentsBuilder) {
        try {
            Long userId = userService.saveUser(userDTO);
            var uri = uriComponentsBuilder.path("/user/{id}")
                    .buildAndExpand(userId).toUri();
            return ResponseEntity.created(uri)
                    .body("User registered successfully with ID: " + userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register user.");
        }

            }

    @GetMapping("")
    public ResponseEntity<Page<UserIdLoginDTO>> listar(Pageable pageable) {
        Page<UserIdLoginDTO> usersPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<String> atualizarUsuario(@PathVariable Long userId, @RequestBody UserIdLoginDTO detalhamentoUserDto) {
        try {
            userService.updateUser(userId, detalhamentoUserDto);
            return ResponseEntity.ok("User updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update user.");
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserIdLoginDTO> detalhar(@PathVariable Long id) {
        // Retrieve user by ID
        Optional<User> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Map user to DetalhamentoUserDto
        User user = optionalUser.get();
        UserIdLoginDTO detalhamentoUserDto = new UserIdLoginDTO(
                user.getId(),
                user.getLogin(),
                user.isStatus()
        );

        return ResponseEntity.ok(detalhamentoUserDto);
    }



}
