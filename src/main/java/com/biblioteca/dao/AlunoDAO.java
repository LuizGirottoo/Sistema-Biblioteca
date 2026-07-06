package com.biblioteca.dao;

import com.biblioteca.model.Aluno;

import java.util.List;
import java.util.Optional;

public interface AlunoDAO {
    Aluno save(Aluno aluno);
    Optional<Aluno> findById(Integer id);
    Optional<Aluno> findByRa(String ra);
    List<Aluno> findAll();
    void updateDebito(Integer id, boolean emDebito);
    void deleteById(Integer id);
}
