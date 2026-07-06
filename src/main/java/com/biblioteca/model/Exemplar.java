package com.biblioteca.model;

/**
 * Representa um exemplar fisico (uma copia) de um Titulo.
 * E o exemplar que efetivamente e emprestado/devolvido, nao o Titulo.
 */
public class Exemplar {

    public enum Situacao {
        DISPONIVEL,
        EMPRESTADO
    }

    private Integer id;
    private Integer tituloId;
    private String codigoTombo;
    private Situacao situacao;

    public Exemplar() {
    }

    public Exemplar(Integer tituloId, String codigoTombo) {
        this.tituloId = tituloId;
        this.codigoTombo = codigoTombo;
        this.situacao = Situacao.DISPONIVEL;
    }

    public Exemplar(Integer id, Integer tituloId, String codigoTombo, Situacao situacao) {
        this.id = id;
        this.tituloId = tituloId;
        this.codigoTombo = codigoTombo;
        this.situacao = situacao;
    }

    public boolean isDisponivel() {
        return this.situacao == Situacao.DISPONIVEL;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTituloId() {
        return tituloId;
    }

    public void setTituloId(Integer tituloId) {
        this.tituloId = tituloId;
    }

    public String getCodigoTombo() {
        return codigoTombo;
    }

    public void setCodigoTombo(String codigoTombo) {
        this.codigoTombo = codigoTombo;
    }

    public Situacao getSituacao() {
        return situacao;
    }

    public void setSituacao(Situacao situacao) {
        this.situacao = situacao;
    }
}
