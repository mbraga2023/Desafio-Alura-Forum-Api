package com.alura.Desafio_Forum.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


public record UsuarioDto(
        @NotBlank String nome,
        @NotBlank @Email String email,
        @NotBlank String senha
) {
}
