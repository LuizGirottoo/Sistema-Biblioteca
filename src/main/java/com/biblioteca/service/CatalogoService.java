package com.biblioteca.service;

import com.biblioteca.dao.ExemplarDAO;
import com.biblioteca.dao.TituloDAO;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Titulo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servico responsavel pelo cadastro de titulos (livros) e seus exemplares.
 */
@Service
public class CatalogoService {

    private final TituloDAO tituloDAO;
    private final ExemplarDAO exemplarDAO;

    public CatalogoService(TituloDAO tituloDAO, ExemplarDAO exemplarDAO) {
        this.tituloDAO = tituloDAO;
        this.exemplarDAO = exemplarDAO;
    }

    public Titulo cadastrarTitulo(Titulo titulo) {
        return tituloDAO.save(titulo);
    }

    public Exemplar cadastrarExemplar(Integer tituloId, String codigoTombo) {
        return exemplarDAO.save(new Exemplar(tituloId, codigoTombo));
    }

    public List<Titulo> listarTitulos() {
        return tituloDAO.findAll();
    }

    public List<Exemplar> listarExemplares() {
        return exemplarDAO.findAll();
    }

    public List<Exemplar> listarExemplaresDisponiveis(Integer tituloId) {
        return exemplarDAO.findDisponiveisPorTitulo(tituloId);
    }
}
