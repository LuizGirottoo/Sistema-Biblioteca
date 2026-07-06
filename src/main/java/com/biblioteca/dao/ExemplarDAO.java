package com.biblioteca.dao;

import com.biblioteca.model.Exemplar;

import java.util.List;
import java.util.Optional;

public interface ExemplarDAO {
    Exemplar save(Exemplar exemplar);
    Optional<Exemplar> findById(Integer id);
    List<Exemplar> findAll();
    List<Exemplar> findDisponiveisPorTitulo(Integer tituloId);
    void updateSituacao(Integer id, Exemplar.Situacao situacao);
    void deleteById(Integer id);
}
