package com.biblioteca.dao;

import com.biblioteca.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemDAO {
    Item save(Item item);
    Optional<Item> findById(Integer id);
    List<Item> findByEmprestimoId(Integer emprestimoId);
    List<Item> findPendentes();
    void registrarDevolucao(Integer itemId, java.time.LocalDate dataDevolucaoEfetiva);
}
