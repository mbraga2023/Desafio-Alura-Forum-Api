package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.UsuarioDto;
import com.alura.Desafio_Forum.dto.response.UsuarioDetalhamentoDto;
import com.alura.Desafio_Forum.dto.response.UsuarioIdDto;
import com.alura.Desafio_Forum.repository.UsuarioRepository;
import com.alura.Desafio_Forum.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private UsuarioService usuarioService;

    @PostMapping("")
    @Transactional
    public ResponseEntity<String> cadastrarUsuario(@RequestBody @Valid UsuarioDto userDTO,
                                                   UriComponentsBuilder uriComponentsBuilder) {
                    Long userId = usuarioService.saveUser(userDTO);
            var uri = uriComponentsBuilder.path("/usuario/{id}")
                    .buildAndExpand(userId).toUri();
            return ResponseEntity.created(uri)
                    .body("Usuário registrado com sucesso. Id: " + userId);
                    }

    @GetMapping("")
    public ResponseEntity<Page<UsuarioIdDto>> listarUsuarios(Pageable pageable) {
        Page<UsuarioIdDto> usersPage = usuarioService.getAllUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }


    @PutMapping("/{userId}")
    public ResponseEntity<String> atualizarUsuario(
            @PathVariable Long userId,
            @RequestBody UsuarioIdDto usuarioInfo) {

            usuarioService.updateUser(userId, usuarioInfo);
            return ResponseEntity.ok("Usuário atualizado com sucesso.");

    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUsuarios(@PathVariable Long userId) {

            usuarioService.deleteUser(userId);
            return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetalhamentoDto> detalharUsuario(@PathVariable Long id) {
        Optional<UsuarioDetalhamentoDto> detalheOptional = usuarioService.detalharUsuario(id);

        return detalheOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
