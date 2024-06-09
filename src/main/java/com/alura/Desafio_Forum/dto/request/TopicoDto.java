package com.alura.Desafio_Forum.dto.request;

public record TopicoDto (String titulo,
                         String mensagem,
                         UsuarioDTO autor,
                         CursoDto curso
                         ){
}
