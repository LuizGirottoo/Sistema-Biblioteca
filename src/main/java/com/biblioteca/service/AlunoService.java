package com.biblioteca.service;

import com.biblioteca.dao.AlunoDAO;
import com.biblioteca.model.Aluno;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servico responsavel pelo cadastro de alunos.
 */
@Service
public class AlunoService {

    private final AlunoDAO alunoDAO;

    public AlunoService(AlunoDAO alunoDAO) {
        this.alunoDAO = alunoDAO;
    }

    public Aluno cadastrar(Aluno aluno) {
        return alunoDAO.save(aluno);
    }

    public List<Aluno> listarTodos() {
        return alunoDAO.findAll();
    }
}
