package com.alura.Desafio_Forum.dto.request;

import com.alura.Desafio_Forum.dto.response.CursoIdDto;
import com.alura.Desafio_Forum.dto.response.UsuarioIdDto;
import jakarta.validation.constraints.NotBlank;

public record TopicoDto (@NotBlank String titulo,
                         @NotBlank String mensagem,
                         UsuarioIdDto autor,
                         CursoIdDto curso
                         ){
}
