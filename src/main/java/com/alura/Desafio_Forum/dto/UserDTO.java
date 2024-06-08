package com.alura.Desafio_Forum.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDTO(

        @Email @NotBlank String login,
        @NotBlank String senha
){
}
