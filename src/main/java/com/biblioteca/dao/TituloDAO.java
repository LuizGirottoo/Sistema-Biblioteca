package com.biblioteca.dao;

import com.biblioteca.model.Titulo;

import java.util.List;
import java.util.Optional;

/**
 * Interface DAO (Data Access Object) para a entidade Titulo.
 * Define o contrato de acesso a dados, independente da tecnologia de persistencia usada.
 */
public interface TituloDAO {
    Titulo save(Titulo titulo);
    Optional<Titulo> findById(Integer id);
    List<Titulo> findAll();
    void deleteById(Integer id);
}
