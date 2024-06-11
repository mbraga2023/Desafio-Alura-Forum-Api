package com.alura.Desafio_Forum.dto.response;

public record TopicosListAtivosDto (
        Long id,
        String titulo,
        String mensagem,
        UsuarioIdDto autor,
        CursoIdDto curso
) {
    public TopicosListAtivosDto(Long id, String titulo, String mensagem, UsuarioIdDto autor, CursoIdDto curso) {
        this.id = id;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.autor = autor;
        this.curso = curso;
    }
}
