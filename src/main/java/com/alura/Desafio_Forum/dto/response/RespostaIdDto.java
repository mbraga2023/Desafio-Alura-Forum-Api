package com.alura.Desafio_Forum.dto.response;

import java.time.LocalDateTime;

public record RespostaIdDto(
        Long id,
        String mensagem,
        LocalDateTime data_criacao,
        boolean solucao,
        Long autorId,
        Long topicId
) {
}
