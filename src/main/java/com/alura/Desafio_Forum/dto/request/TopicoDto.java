package com.alura.Desafio_Forum.dto.request;

import com.alura.Desafio_Forum.dto.response.CursoIdDto;
import com.alura.Desafio_Forum.dto.response.UsuarioIdDto;

public record TopicoDto (String titulo,
                         String mensagem,
                         UsuarioIdDto autor,
                         CursoIdDto curso
                         ){
}
