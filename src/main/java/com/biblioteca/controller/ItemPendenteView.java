package com.biblioteca.controller;

import java.time.LocalDate;

/**
 * DTO simples usado apenas para exibir, na tela de devolucao, os dados
 * relevantes de um item de emprestimo ainda pendente.
 *
 * Implementado como classe (com getters) em vez de record para garantir
 * compatibilidade com o acesso a propriedades via Thymeleaf/SpEL.
 */
public class ItemPendenteView {

    private final Integer itemId;
    private final String codigoTombo;
    private final String tituloNome;
    private final String alunoNome;
    private final String alunoRa;
    private final LocalDate dataDevolucaoPrevista;
    private final boolean atrasado;

    public ItemPendenteView(Integer itemId, String codigoTombo, String tituloNome, String alunoNome,
                             String alunoRa, LocalDate dataDevolucaoPrevista, boolean atrasado) {
        this.itemId = itemId;
        this.codigoTombo = codigoTombo;
        this.tituloNome = tituloNome;
        this.alunoNome = alunoNome;
        this.alunoRa = alunoRa;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.atrasado = atrasado;
    }

    public Integer getItemId() {
        return itemId;
    }

    public String getCodigoTombo() {
        return codigoTombo;
    }

    public String getTituloNome() {
        return tituloNome;
    }

    public String getAlunoNome() {
        return alunoNome;
    }

    public String getAlunoRa() {
        return alunoRa;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public boolean isAtrasado() {
        return atrasado;
    }
}
