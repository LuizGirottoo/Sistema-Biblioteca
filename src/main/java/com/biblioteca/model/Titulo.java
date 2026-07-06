package com.biblioteca.model;

/**
 * Representa o titulo de uma obra (o "livro" conceitual).
 * Cada Titulo pode ter varios Exemplares (copias fisicas) associados.
 */
public class Titulo {

    private Integer id;
    private String nome;
    private String autor;
    private String isbn;
    private int prazoEmprestimoDias; // prazo padrao de emprestimo, em dias

    public Titulo() {
    }

    public Titulo(String nome, String autor, String isbn, int prazoEmprestimoDias) {
        this.nome = nome;
        this.autor = autor;
        this.isbn = isbn;
        this.prazoEmprestimoDias = prazoEmprestimoDias;
    }

    public Titulo(Integer id, String nome, String autor, String isbn, int prazoEmprestimoDias) {
        this.id = id;
        this.nome = nome;
        this.autor = autor;
        this.isbn = isbn;
        this.prazoEmprestimoDias = prazoEmprestimoDias;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPrazoEmprestimoDias() {
        return prazoEmprestimoDias;
    }

    public void setPrazoEmprestimoDias(int prazoEmprestimoDias) {
        this.prazoEmprestimoDias = prazoEmprestimoDias;
    }
}
