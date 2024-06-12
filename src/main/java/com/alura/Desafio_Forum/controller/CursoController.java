package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.dto.request.CursoDto;
import com.alura.Desafio_Forum.dto.response.CursoIdDto;
import com.alura.Desafio_Forum.service.CursoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/curso")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoService service;

    @PostMapping("")
    @Transactional
    public ResponseEntity<String> cadastrarCurso(@RequestBody @Valid CursoDto cursoDto,
                                                 UriComponentsBuilder uriComponentsBuilder) {

            Long cursoId = service.saveCurso(cursoDto);
            var uri = uriComponentsBuilder.path("/curso/{id}")
                    .buildAndExpand(cursoId)
                    .toUri();
            return ResponseEntity.created(uri)
                    .body("Curso registrado com sucesso");

    }


    @GetMapping("")
    public ResponseEntity<Page<CursoIdDto>> listarCursos(Pageable pageable) {
        Page<CursoIdDto> cursosPage = service.getAllCursos(pageable);
        return ResponseEntity.ok(cursosPage);
    }


    @PutMapping("/{cursoId}")
    public ResponseEntity<String> atualizarCurso(
            @PathVariable Long cursoId,
            @RequestBody CursoIdDto cursoIdDto) {

            service.updateCurso(cursoId, cursoIdDto);
            return ResponseEntity.ok("Curso atualizado com sucesso.");

    }

}
