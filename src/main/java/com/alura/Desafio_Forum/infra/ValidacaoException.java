package br.com.alura.med.voll.alura_medVoll_api.infra;

public class ValidacaoException extends RuntimeException {
    public ValidacaoException(String mensagem) {
        super (mensagem);
    }
}
