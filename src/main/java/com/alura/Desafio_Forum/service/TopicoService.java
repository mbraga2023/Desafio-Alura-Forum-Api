package com.alura.Desafio_Forum.service;

import com.alura.Desafio_Forum.domain.Topico;
import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.TopicoDto;
import com.alura.Desafio_Forum.dto.request.UsuarioDTO;
import com.alura.Desafio_Forum.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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

   /* @Transactional
    public Long saveTopico(TopicoDto topicoDto) {
        if (topicoDto.titulo() == null || topicoDto.mensagem() == null) {
            throw new IllegalArgumentException("Título e Mensagem obrigatórios.");
        }

        // Retrieve the currently authenticated user
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = userDetails.getUsername();

        // Assuming you have a method to retrieve the Usuario based on the email
        Usuario loggedUser = usuarioService.findByEmail(email);

        Topico topico = new Topico();
        topico.setTitulo(topicoDto.titulo());
        topico.setMensagem(topicoDto.mensagem());
        topico.setAutor(loggedUser);
        topico.setCurso(topicoDto.curso());

        // Set the current date and time
        topico.setDataCriacao(LocalDateTime.now());

        topico.setStatus(true);
        Topico savedTopico = repository.save(topico);
        return savedTopico.getId();
    }*/
    }
