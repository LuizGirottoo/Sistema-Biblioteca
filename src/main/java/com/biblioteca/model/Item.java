package com.biblioteca.model;

import java.time.LocalDate;

/**
 * Representa um item de um emprestimo: a associacao entre um Emprestimo
 * e um Exemplar especifico, com sua data de devolucao prevista e efetiva.
 */
public class Item {

    private Integer id;
    private Integer emprestimoId;
    private Integer exemplarId;
    private LocalDate dataDevolucaoPrevista;
    private LocalDate dataDevolucaoEfetiva; // null enquanto nao devolvido

    public Item() {
    }

    public Item(Integer exemplarId, LocalDate dataDevolucaoPrevista) {
        this.exemplarId = exemplarId;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public Item(Integer id, Integer emprestimoId, Integer exemplarId,
                LocalDate dataDevolucaoPrevista, LocalDate dataDevolucaoEfetiva) {
        this.id = id;
        this.emprestimoId = emprestimoId;
        this.exemplarId = exemplarId;
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
    }

    public boolean isDevolvido() {
        return dataDevolucaoEfetiva != null;
    }

    public boolean isAtrasado(LocalDate hoje) {
        return !isDevolvido() && hoje.isAfter(dataDevolucaoPrevista);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmprestimoId() {
        return emprestimoId;
    }

    public void setEmprestimoId(Integer emprestimoId) {
        this.emprestimoId = emprestimoId;
    }

    public Integer getExemplarId() {
        return exemplarId;
    }

    public void setExemplarId(Integer exemplarId) {
        this.exemplarId = exemplarId;
    }

    public LocalDate getDataDevolucaoPrevista() {
        return dataDevolucaoPrevista;
    }

    public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
        this.dataDevolucaoPrevista = dataDevolucaoPrevista;
    }

    public LocalDate getDataDevolucaoEfetiva() {
        return dataDevolucaoEfetiva;
    }

    public void setDataDevolucaoEfetiva(LocalDate dataDevolucaoEfetiva) {
        this.dataDevolucaoEfetiva = dataDevolucaoEfetiva;
    }
}
