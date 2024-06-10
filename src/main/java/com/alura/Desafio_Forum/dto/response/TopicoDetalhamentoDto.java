package com.alura.Desafio_Forum.dto.response;

public record TopicoDetalhamentoDto (
        Long id,
        String titulo,
        String mensagem,
        UsuarioIdDto autor,
        CursoIdDto curso
) {
}
