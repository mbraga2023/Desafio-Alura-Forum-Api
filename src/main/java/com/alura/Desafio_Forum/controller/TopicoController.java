package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.RespostaDto;
import com.alura.Desafio_Forum.dto.request.TopicoDto;
import com.alura.Desafio_Forum.dto.response.TopicoDetalhamentoDto;
import com.alura.Desafio_Forum.dto.response.TopicoListDto;
import com.alura.Desafio_Forum.dto.response.TopicosListAtivosDto;
import com.alura.Desafio_Forum.repository.TopicoRepository;
import com.alura.Desafio_Forum.service.RespostaService;
import com.alura.Desafio_Forum.service.TopicoService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {


    @Autowired
    private TopicoService service;

    @Autowired
    private RespostaService respostaService;

    @PostMapping("")
    public ResponseEntity<String> cadastrarTopico(
            @RequestBody @Valid TopicoDto topicoDto,
            UriComponentsBuilder uriComponentsBuilder,
            Authentication authentication) {

        // Get the logged-in user's email
        String loggedUserEmail = authentication.getName();

        // Save the topico using the service method with the logged-in user's email
        Long topicoId = service.saveTopico(topicoDto, loggedUserEmail);

        // Build the URI for the newly created resource
        var uri = uriComponentsBuilder.path("/topicos/{id}")
                .buildAndExpand(topicoId).toUri();

        // Return the response with the URI of the newly created resource
        return ResponseEntity.created(uri)
                .body("Tópico registrado com sucesso. Id: " + topicoId);
    }

    @GetMapping("/lista")
    public ResponseEntity<Page<TopicosListAtivosDto>> listarTopicosAtivos(
            @RequestParam(required = false) String cursoNome,
            @RequestParam(required = false) Integer ano,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TopicosListAtivosDto> topicosPage = service.getAllTopicosAtivos(pageable, cursoNome, ano);
        return ResponseEntity.ok(topicosPage);
    }

    @GetMapping("listaAdmin")
    public ResponseEntity<Page<TopicoListDto>> listarTodosTopicos(
            @RequestParam(required = false) String cursoNome,
            @RequestParam(required = false) Integer ano,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TopicoListDto> topicosPage = service.getAllTopicosOrderByDataCriacao(pageable, cursoNome, ano);
        return ResponseEntity.ok(topicosPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalharTopico(@PathVariable Long id){
        Optional<TopicoDetalhamentoDto> detalheOptional = service.detalharTopico(id);

        return detalheOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{topicoId}")
    public ResponseEntity<String> atualizarTopico(
            @PathVariable Long topicoId,
            @RequestBody TopicoDetalhamentoDto topicoInfo) {

        service.updateUser(topicoId, topicoInfo);
        return ResponseEntity.ok("Tópico atualizado com sucesso.");

    }

    /*    EXCLUSÃO FÍSICA
    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
    medicoRepository.deleteById(id);
    }*/

    //exclusão lógica
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluirTopico(@PathVariable Long id){
        service.inactivateTopico(id); // Call the method to inactivate the topic
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resposta/{topicId}")
    public ResponseEntity<?> salvarResposta(
            @PathVariable Long topicId,
            @RequestBody RespostaDto respostaDto,
            Principal principal) {
        Usuario autor = respostaService.findByEmail(principal.getName());
        LocalDateTime dataCriacao = LocalDateTime.now();
        respostaService.saveResposta(topicId, respostaDto, autor, dataCriacao);
        return ResponseEntity.ok().build();
    }

}




