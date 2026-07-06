package com.biblioteca.exception;

/**
 * Excecao lancada quando uma regra de negocio do dominio da biblioteca e violada
 * (ex: aluno inexistente, aluno em debito, exemplar indisponivel).
 */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
