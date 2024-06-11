package com.alura.Desafio_Forum.dto.request;

import com.alura.Desafio_Forum.domain.Resposta;
import com.alura.Desafio_Forum.domain.Topico;
import com.alura.Desafio_Forum.domain.Usuario;

import java.time.LocalDateTime;

public record RespostaDto (
        String mensagem,
        boolean solucao,
        Long autorId,
        Long topicoId
) {
    public Resposta toEntity(Usuario autor, Topico topico, LocalDateTime dataCriacao) {
        Resposta resposta = new Resposta();
        resposta.setMensagem(this.mensagem());
        resposta.setSolucao(this.solucao());
        resposta.setAutor(autor);
        resposta.setTopico(topico);
        resposta.setData_criacao(dataCriacao);
        return resposta;
    }
}

