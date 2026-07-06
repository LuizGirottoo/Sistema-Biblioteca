package com.biblioteca.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um emprestimo realizado por um aluno.
 * Um emprestimo agrega um ou mais Itens (cada um associado a um Exemplar).
 */
public class Emprestimo {

    public enum Status {
        ATIVO,
        FINALIZADO
    }

    private Integer id;
    private Integer alunoId;
    private LocalDate dataEmprestimo;
    private Status status;
    private List<Item> itens = new ArrayList<>();

    public Emprestimo() {
    }

    public Emprestimo(Integer alunoId, LocalDate dataEmprestimo) {
        this.alunoId = alunoId;
        this.dataEmprestimo = dataEmprestimo;
        this.status = Status.ATIVO;
    }

    public Emprestimo(Integer id, Integer alunoId, LocalDate dataEmprestimo, Status status) {
        this.id = id;
        this.alunoId = alunoId;
        this.dataEmprestimo = dataEmprestimo;
        this.status = status;
    }

    public void adicionarItem(Item item) {
        this.itens.add(item);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Integer alunoId) {
        this.alunoId = alunoId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void setItens(List<Item> itens) {
        this.itens = itens;
    }
}
