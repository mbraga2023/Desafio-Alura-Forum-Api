package com.alura.Desafio_Forum.service;

import com.alura.Desafio_Forum.domain.Curso;
import com.alura.Desafio_Forum.domain.Topico;
import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.TopicoDto;
import com.alura.Desafio_Forum.dto.response.*;
import com.alura.Desafio_Forum.repository.TopicoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    @Transactional
    public Long saveTopico(TopicoDto topicoDto, String loggedUserEmail) {
        if (topicoDto.titulo() == null || topicoDto.mensagem() == null) {
            throw new IllegalArgumentException("Título e Mensagem são obrigatórios.");
        }

        // Find the logged-in user
        Usuario loggedUser = usuarioService.findByEmail(loggedUserEmail);

        // Check if titulo, mensagem, and cursoId combination already exists
        if (repository.existsByTituloAndMensagemAndCursoId(topicoDto.titulo(), topicoDto.mensagem(), topicoDto.curso().id())) {
            throw new IllegalArgumentException("Combinação de Título, Mensagem e Curso já existe.");
        }

        // Check if cursoId is valid (non-null and exists)
        Long cursoId = topicoDto.curso().id();
        if (cursoId == null || !cursoService.existsById(cursoId)) {
            throw new IllegalArgumentException("O ID do Curso não é válido");
        }

        // Create and save the Topico entity
        Topico topico = new Topico();
        topico.setTitulo(topicoDto.titulo());
        topico.setMensagem(topicoDto.mensagem());
        topico.setAutor(loggedUser);
        topico.setCurso(new Curso(cursoId, topicoDto.curso().nome(), topicoDto.curso().categoria()));
        topico.setData_criacao(LocalDateTime.now());
        topico.setStatus(true);

        Topico savedTopico = repository.save(topico);
        return savedTopico.getId();
    }

    public Page<TopicoListDto> getAllTopicosOrderByDataCriacao(Pageable pageable, String cursoNome, Integer ano) {
        Page<Topico> topicosPage;

        if (cursoNome != null && ano != null) {
            topicosPage = repository.findByCursoNomeAndAno(cursoNome, ano, pageable);
        } else {
            topicosPage = repository.findAllByOrderByDataCriacaoAsc(pageable);
        }

        return topicosPage.map(topico -> new TopicoListDto(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                new UsuarioIdDto(topico.getAutor().getId(), topico.getAutor().getNome(), topico.getAutor().getEmail()),
                new CursoIdDto(topico.getCurso().getId(), topico.getCurso().getNome(), topico.getCurso().getCategoria()),
                topico.isStatus()
        ));
    }


    public void updateUser(Long topicoId, TopicoDetalhamentoDto topicoInfo) {
        Optional<Topico> optionalTopico = repository.findById(topicoId);
        if (optionalTopico.isEmpty()) {
            throw new IllegalStateException("Tópico não encontrado");
        }

        Topico existingTopico = optionalTopico.get();

        existingTopico.setTitulo(topicoInfo.titulo());
        existingTopico.setMensagem(topicoInfo.mensagem());

        try {
            repository.save(existingTopico);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Título ou mensagem não podem ser nulos", e);
        }
    }

    @Transactional
    public void inactivateTopico(Long id) {
        // Find the topic by its ID
        Topico topic = repository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Tópico não encontrado"));

        // Set the status to false
        topic.setStatus(false);

        // Save the updated topic
        repository.save(topic);
    }


    public Page<TopicosListAtivosDto> getAllTopicosAtivos(Pageable pageable, String cursoNome, Integer ano) {
        Page<Topico> topicosPage;

        // Only filter by status true
        topicosPage = repository.findByStatusTrue(pageable);

        return topicosPage.map(topico -> new TopicosListAtivosDto(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                new UsuarioIdDto(topico.getAutor().getId(), topico.getAutor().getNome(), topico.getAutor().getEmail()),
                new CursoIdDto(topico.getCurso().getId(), topico.getCurso().getNome(), topico.getCurso().getCategoria())
        ));
    }
    public Topico findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tópico não encontrado"));
    }

    public Optional<TopicoDetalhamentoDto> detalharTopico(Long id) {
        Optional<Topico> optionalTopico = repository.findByIdAndStatusTrue(id);
        return optionalTopico.map(this::mapToDetalhamentoDto);
    }

    private TopicoDetalhamentoDto mapToDetalhamentoDto(Topico topico) {
        List<RespostaIdDto> respostasDto = topico.getRespostas().stream()
                .map(resposta -> new RespostaIdDto(
                        resposta.getId(),
                        resposta.getMensagem(),
                        resposta.getData_criacao(),
                        resposta.isSolucao(),
                        resposta.getAutor().getId(),
                        resposta.getTopico().getId()
                ))
                .collect(Collectors.toList());

        return new TopicoDetalhamentoDto(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                new UsuarioIdDto(topico.getAutor().getId(), topico.getAutor().getNome(), topico.getAutor().getEmail()),
                new CursoIdDto(topico.getCurso().getId(), topico.getCurso().getNome(), topico.getCurso().getCategoria()),
                respostasDto,
                topico.isStatus()
        );
    }
}