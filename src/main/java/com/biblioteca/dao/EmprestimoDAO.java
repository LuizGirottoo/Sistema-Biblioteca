package com.biblioteca.dao;

import com.biblioteca.model.Emprestimo;

import java.util.List;
import java.util.Optional;

public interface EmprestimoDAO {
    Emprestimo save(Emprestimo emprestimo);
    Optional<Emprestimo> findById(Integer id);
    List<Emprestimo> findAtivosPorAluno(Integer alunoId);
    List<Emprestimo> findAll();
    void atualizarStatus(Integer id, Emprestimo.Status status);
}
