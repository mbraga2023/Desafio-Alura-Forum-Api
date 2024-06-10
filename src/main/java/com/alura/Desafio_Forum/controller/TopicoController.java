package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.dto.request.TopicoDto;
import com.alura.Desafio_Forum.repository.TopicoRepository;
import com.alura.Desafio_Forum.service.TopicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topico")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private TopicoService service;

    @PostMapping("")
    @Transactional
    public ResponseEntity<String> cadastrarTopico(
            @RequestBody @Valid TopicoDto topicoDto,
            UriComponentsBuilder uriComponentsBuilder) {

            Long topicoId = service.saveTopico(topicoDto);
            var uri = uriComponentsBuilder.path("/topico/{id}")
                    .buildAndExpand(topicoId).toUri();
            return ResponseEntity.created(uri)
                    .body("Tópico registered successfully with ID: " + topicoId);

    }

}




