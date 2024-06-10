package com.alura.Desafio_Forum.service;

import com.alura.Desafio_Forum.domain.Curso;
import com.alura.Desafio_Forum.domain.Topico;
import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.TopicoDto;
import com.alura.Desafio_Forum.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        // Check if titulo already exists
        if (repository.existsByTitulo(topicoDto.titulo())) {
            throw new IllegalArgumentException("Título já existe.");
        }

        // Check if mensagem already exists
        if (repository.existsByMensagem(topicoDto.mensagem())) {
            throw new IllegalArgumentException("Mensagem já existe.");
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

}
