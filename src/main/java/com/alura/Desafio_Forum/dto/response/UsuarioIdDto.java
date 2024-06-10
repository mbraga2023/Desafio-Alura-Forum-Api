package com.alura.Desafio_Forum.dto.response;

public record UsuarioIdDto(Long id,
                           String nome,
                           String email,
                           boolean status){
    public UsuarioIdDto(Long id, String nome, String email) {
        this(id, nome, email, false);
    }
}
