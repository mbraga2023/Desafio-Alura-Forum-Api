package com.alura.Desafio_Forum.dto.response;

import java.util.List;

public record TopicoListDto(
        Long id,
        String titulo,
        String mensagem,
        UsuarioIdDto autor,
        CursoIdDto curso,
        boolean status
) {
    public TopicoListDto(Long id, String titulo, String mensagem, UsuarioIdDto autor, CursoIdDto curso, boolean status) {
        this.id = id;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.autor = autor;
        this.curso = curso;
        this.status = status;
    }
}
