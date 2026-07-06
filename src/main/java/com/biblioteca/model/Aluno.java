package com.biblioteca.model;

/**
 * Representa um aluno cadastrado no sistema de biblioteca.
 */
public class Aluno {

    private Integer id;
    private String ra;
    private String nome;
    private String email;
    private boolean emDebito;

    public Aluno() {
    }

    public Aluno(String ra, String nome, String email) {
        this.ra = ra;
        this.nome = nome;
        this.email = email;
        this.emDebito = false;
    }

    public Aluno(Integer id, String ra, String nome, String email, boolean emDebito) {
        this.id = id;
        this.ra = ra;
        this.nome = nome;
        this.email = email;
        this.emDebito = emDebito;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmDebito() {
        return emDebito;
    }

    public void setEmDebito(boolean emDebito) {
        this.emDebito = emDebito;
    }
}
