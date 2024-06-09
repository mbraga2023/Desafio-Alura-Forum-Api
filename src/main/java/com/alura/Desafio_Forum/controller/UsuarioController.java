package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.UsuarioDTO;
import com.alura.Desafio_Forum.dto.response.UsuarioDetalhamentoDto;
import com.alura.Desafio_Forum.dto.response.UsuarioIdEmailDto;
import com.alura.Desafio_Forum.repository.UsuarioRepository;
import com.alura.Desafio_Forum.service.UsuarioService;
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
@RequestMapping("/usuario")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("")
    @Transactional
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid UsuarioDTO userDTO,
                                                   UriComponentsBuilder uriComponentsBuilder) {
        try {
            Long userId = usuarioService.saveUser(userDTO);
            var uri = uriComponentsBuilder.path("/usuario/{id}")
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
    public ResponseEntity<Page<UsuarioIdEmailDto>> listar(Pageable pageable) {
        Page<UsuarioIdEmailDto> usersPage = usuarioService.getAllUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<String> atualizarUsuario(
            @PathVariable Long userId,
            @RequestBody UsuarioIdEmailDto usuarioInfo) {
        try {
            usuarioService.updateUser(userId, usuarioInfo);
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
            usuarioService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalhamentoDto> detalhar(@PathVariable Long id) {
        // Retrieve user by ID
        Optional<Usuario> optionalUser = repository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Map user to DetalhamentoUserDto
        Usuario user = optionalUser.get();
        UsuarioDetalhamentoDto detalhamentoUserDto = new UsuarioDetalhamentoDto(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.isStatus()
        );

        return ResponseEntity.ok(detalhamentoUserDto);
    }



}
