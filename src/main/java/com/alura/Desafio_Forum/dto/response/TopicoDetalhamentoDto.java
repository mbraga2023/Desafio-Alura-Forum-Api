package com.alura.Desafio_Forum.dto.response;

import com.alura.Desafio_Forum.domain.Topico;

public record TopicoDetalhamentoDto (
        Long id,
        String titulo,
        String mensagem,
        UsuarioIdDto autor,
        CursoIdDto curso,
        boolean status
) {
    public TopicoDetalhamentoDto(Long id, String titulo, String mensagem, UsuarioIdDto autor, CursoIdDto curso, boolean status) {
        this.id = id;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.autor = autor;
        this.curso = curso;
        this.status = true;
    }

}
