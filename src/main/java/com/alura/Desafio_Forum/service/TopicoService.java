package com.alura.Desafio_Forum.service;

import com.alura.Desafio_Forum.domain.Curso;
import com.alura.Desafio_Forum.domain.Topico;
import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.TopicoDto;
import com.alura.Desafio_Forum.dto.response.CursoIdDto;
import com.alura.Desafio_Forum.dto.response.TopicoDetalhamentoDto;
import com.alura.Desafio_Forum.dto.response.UsuarioIdDto;
import com.alura.Desafio_Forum.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TopicoService {

    @Autowired
    private TopicoRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    @Transactional
    public Long saveTopico(TopicoDto topicoDto) {
        if (topicoDto.titulo() == null || topicoDto.mensagem() == null || topicoDto.autor() == null) {
            throw new IllegalArgumentException("Título, Mensagem e Autor são obrigatórios.");
        }

        // Retrieve the currently authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedUserEmail = userDetails.getUsername();

        // Find the logged-in user
        Usuario loggedUser = usuarioService.findByEmail(loggedUserEmail);

        // Check if the provided email matches the logged-in user's email
        String providedEmail = topicoDto.autor().email();
        if (!loggedUserEmail.equals(providedEmail)) {
            throw new IllegalArgumentException("O email fornecido não é válido.");
        }

        // Check if titulo, mensagem, and cursoId combination already exists
        if (repository.existsByTituloAndMensagemAndCursoId(topicoDto.titulo(), topicoDto.mensagem(), topicoDto.curso().id())) {
            throw new IllegalArgumentException("Combinação de Título, Mensagem e Curso já existe.");
        }

        Topico topico = new Topico();
        topico.setTitulo(topicoDto.titulo());
        topico.setMensagem(topicoDto.mensagem());
        topico.setAutor(loggedUser);

        Long cursoId = topicoDto.curso().id();

        // Check if cursoId is valid (non-null and exists)
        if (cursoId == null || !cursoService.existsById(cursoId)) {
            throw new IllegalArgumentException("O ID do Curso não é válido");
        }

        topico.setCurso(new Curso(topicoDto.curso().id(), topicoDto.curso().nome(),
                topicoDto.curso().categoria()));

        // Set the current date and time
        topico.setData_criacao(LocalDateTime.now());

        topico.setStatus(true);
        Topico savedTopico = repository.save(topico);
        return savedTopico.getId();
    }


    public Page<TopicoDetalhamentoDto> getAllTopicosOrderByDataCriacao(Pageable pageable, String cursoNome, Integer ano) {
        Page<Topico> topicosPage;

        if (cursoNome != null && ano != null) {
            topicosPage = repository.findByCursoNomeAndAno(cursoNome, ano, pageable);
        } else {
            topicosPage = repository.findAllByOrderByDataCriacaoAsc(pageable);
        }

        return topicosPage.map(topico -> new TopicoDetalhamentoDto(
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
}
