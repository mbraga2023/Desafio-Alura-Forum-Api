package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.dto.request.CursoDto;
import com.alura.Desafio_Forum.dto.response.CursoIdDto;
import com.alura.Desafio_Forum.repository.CursoRepository;
import com.alura.Desafio_Forum.service.CursoService;
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

@RestController
@RequestMapping("/curso")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @Autowired
    private CursoService service;

    @PostMapping("")
    @Transactional
    public ResponseEntity<String> cadastrarCurso(@RequestBody @Valid CursoDto cursoDto,
                                                 UriComponentsBuilder uriComponentsBuilder) {
        try {
            Long cursoId = service.saveCurso(cursoDto);
            var uri = uriComponentsBuilder.path("/curso/{id}")
                    .buildAndExpand(cursoId)
                    .toUri();
            return ResponseEntity.created(uri)
                    .body("Curso registered successfully with ID: " + cursoId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register curso.");
        }
    }


    @GetMapping("")
    public ResponseEntity<Page<CursoIdDto>> listar(Pageable pageable) {
        Page<CursoIdDto> cursosPage = service.getAllCursos(pageable);
        return ResponseEntity.ok(cursosPage);
    }


    @PutMapping("/{cursoId}")
    public ResponseEntity<String> atualizarCurso(
            @PathVariable Long cursoId,
            @RequestBody CursoIdDto cursoIdDto) {
        try {
            service.updateCurso(cursoId, cursoIdDto);
            return ResponseEntity.ok("Curso updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update curso.");
        }
    }

}
