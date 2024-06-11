package com.alura.Desafio_Forum.controller;

import com.alura.Desafio_Forum.domain.Topico;
import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.RespostaDto;
import com.alura.Desafio_Forum.dto.request.TopicoDto;
import com.alura.Desafio_Forum.dto.response.CursoIdDto;
import com.alura.Desafio_Forum.dto.response.TopicoDetalhamentoDto;
import com.alura.Desafio_Forum.dto.response.TopicosListAtivosDto;
import com.alura.Desafio_Forum.dto.response.UsuarioIdDto;
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
    private TopicoRepository repository;

    @Autowired
    private TopicoService service;

    @Autowired
    private RespostaService respostaService;

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
    public ResponseEntity<Page<TopicoDetalhamentoDto>> listarTodos(
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

    /*    EXCLUSÃO FÍSICA
    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id){
    medicoRepository.deleteById(id);
    }*/

    //exclusão lógica
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id){
        service.inactivateTopico(id); // Call the method to inactivate the topic
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resposta/{topicId}")
    public ResponseEntity<?> salvarResposta(
            @PathVariable Long topicId,
            @RequestBody RespostaDto respostaDto,
            Principal principal) {
        Usuario autor = respostaService.findByEmail(principal.getName()); // Assuming principal is the logged-in user
        LocalDateTime dataCriacao = LocalDateTime.now();
        respostaService.saveResposta(topicId, respostaDto, autor, dataCriacao);
        return ResponseEntity.ok().build();
    }



}




