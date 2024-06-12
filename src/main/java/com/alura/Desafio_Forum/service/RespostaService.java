package com.alura.Desafio_Forum.service;

import com.alura.Desafio_Forum.domain.Resposta;
import com.alura.Desafio_Forum.domain.Topico;
import com.alura.Desafio_Forum.domain.Usuario;
import com.alura.Desafio_Forum.dto.request.RespostaDto;
import com.alura.Desafio_Forum.repository.RespostaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoService topicoService;

    @Autowired
    private UsuarioService usuarioService;

    public Usuario findByEmail(String email) {
        return usuarioService.findByEmail(email);
    }

    @Transactional
    public void saveResposta(Long topicId, RespostaDto respostaDto, Usuario autor, LocalDateTime dataCriacao) {
        // Retrieve the associated Topico
        Topico topico = topicoService.findById(topicId);

        // Check if the topic's status is true
        if (topico.isStatus()) {
            // Save the Resposta
            Resposta resposta = respostaDto.toEntity(autor, topico, dataCriacao);
            respostaRepository.save(resposta);
        } else {
            // Handle the case where the topic's status is false (optional)
            throw new IllegalArgumentException("Tópico inativo. Resposta não foi salva");
        }
    }

}
