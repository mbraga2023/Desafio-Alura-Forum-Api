package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.dto.request.TopicoDto;
import com.alura.Desafio_Forum.dto.request.UsuarioDTO;
import com.alura.Desafio_Forum.repository.CursoRepository;
import com.alura.Desafio_Forum.repository.TopicoRepository;
import com.alura.Desafio_Forum.service.CursoService;
import com.alura.Desafio_Forum.service.TopicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/curso")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private TopicoService service;

    /*@PostMapping("")
    @Transactional
    public ResponseEntity<String> cadastrarUsuario(
            @RequestBody @Valid TopicoDto topicoDto,
            UriComponentsBuilder uriComponentsBuilder) {
        try {
            Long topicoId = service.saveTopico(topicoDto);
            var uri = uriComponentsBuilder.path("/topico/{id}")
                    .buildAndExpand(topicoId).toUri();
            return ResponseEntity.created(uri)
                    .body("Tópico registered successfully with ID: " + topicoId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register tópico.");
        }

    }*/

}
