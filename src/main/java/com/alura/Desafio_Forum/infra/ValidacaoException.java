package com.alura.Desafio_Forum.infra;

public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String mensagem) {
        super (mensagem);
    }
}
