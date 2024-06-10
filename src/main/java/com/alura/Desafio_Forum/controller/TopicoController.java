package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.domain.Topico;
import com.alura.Desafio_Forum.dto.request.TopicoDto;
import com.alura.Desafio_Forum.dto.response.CursoIdDto;
import com.alura.Desafio_Forum.dto.response.TopicoDetalhamentoDto;
import com.alura.Desafio_Forum.dto.response.UsuarioIdDto;
import com.alura.Desafio_Forum.repository.TopicoRepository;
import com.alura.Desafio_Forum.service.TopicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

@RestController
@RequestMapping("/topicos")
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
                    .body("Tópico registrado com sucesso. Id: " + topicoId);

    }

    @GetMapping("")
    public ResponseEntity<Page<TopicoDetalhamentoDto>> listar(
            @RequestParam(required = false) String cursoNome,
            @RequestParam(required = false) Integer ano,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TopicoDetalhamentoDto> topicosPage = service.getAllTopicosOrderByDataCriacao(pageable, cursoNome, ano);
        return ResponseEntity.ok(topicosPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id){
        Optional<Topico> optionalTopico = repository.findById(id);

        if (((Optional<?>) optionalTopico).isPresent()) {
            Topico topico = optionalTopico.get();
            TopicoDetalhamentoDto detalhamentoDto = new TopicoDetalhamentoDto(
                    topico.getId(),
                    topico.getTitulo(),
                    topico.getMensagem(),
                    new UsuarioIdDto(topico.getAutor().getId(), topico.getAutor().getNome(), topico.getAutor().getEmail()),
                    new CursoIdDto(topico.getCurso().getId(), topico.getCurso().getNome(), topico.getCurso().getCategoria()),
                    topico.isStatus()
            );

            return ResponseEntity.ok(detalhamentoDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{topicoId}")
    public ResponseEntity<String> atualizarUsuario(
            @PathVariable Long topicoId,
            @RequestBody TopicoDetalhamentoDto topicoInfo) {

        service.updateUser(topicoId, topicoInfo);
        return ResponseEntity.ok("Tópico atualizado com sucesso.");

    }



}




