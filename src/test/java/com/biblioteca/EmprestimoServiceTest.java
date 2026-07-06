package com.biblioteca;

import com.biblioteca.dao.AlunoDAO;
import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.dao.ExemplarDAO;
import com.biblioteca.dao.ItemDAO;
import com.biblioteca.dao.TituloDAO;
import com.biblioteca.exception.RegraNegocioException;
import com.biblioteca.model.Aluno;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Item;
import com.biblioteca.model.Titulo;
import com.biblioteca.service.EmprestimoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Testes unitarios do Caso de Uso "Emprestar Livro".
 * As DAOs sao mockadas para isolar a regra de negocio da persistencia.
 */
class EmprestimoServiceTest {

    private AlunoDAO alunoDAO;
    private ExemplarDAO exemplarDAO;
    private TituloDAO tituloDAO;
    private EmprestimoDAO emprestimoDAO;
    private ItemDAO itemDAO;
    private EmprestimoService service;

    @BeforeEach
    void setUp() {
        alunoDAO = mock(AlunoDAO.class);
        exemplarDAO = mock(ExemplarDAO.class);
        tituloDAO = mock(TituloDAO.class);
        emprestimoDAO = mock(EmprestimoDAO.class);
        itemDAO = mock(ItemDAO.class);
        service = new EmprestimoService(alunoDAO, exemplarDAO, tituloDAO, emprestimoDAO, itemDAO);
    }

    @Test
    void deveRecusarEmprestimoParaAlunoInexistente() {
        when(alunoDAO.findByRa("999")).thenReturn(Optional.empty());

        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.emprestar("999", List.of(1)));
        assertTrue(ex.getMessage().contains("inexistente"));
    }

    @Test
    void deveRecusarEmprestimoParaAlunoEmDebito() {
        Aluno aluno = new Aluno(1, "123", "Fulano", "fulano@uel.br", true);
        when(alunoDAO.findByRa("123")).thenReturn(Optional.of(aluno));

        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.emprestar("123", List.of(1)));
        assertTrue(ex.getMessage().contains("debito"));
    }

    @Test
    void deveRecusarEmprestimoSemExemplaresInformados() {
        Aluno aluno = new Aluno(1, "123", "Fulano", "fulano@uel.br", false);
        when(alunoDAO.findByRa("123")).thenReturn(Optional.of(aluno));

        assertThrows(RegraNegocioException.class, () -> service.emprestar("123", List.of()));
    }

    @Test
    void deveRecusarEmprestimoDeExemplarIndisponivel() {
        Aluno aluno = new Aluno(1, "123", "Fulano", "fulano@uel.br", false);
        when(alunoDAO.findByRa("123")).thenReturn(Optional.of(aluno));

        Exemplar exemplarEmprestado = new Exemplar(10, 1, "T-001", Exemplar.Situacao.EMPRESTADO);
        when(exemplarDAO.findById(10)).thenReturn(Optional.of(exemplarEmprestado));

        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.emprestar("123", List.of(10)));
        assertTrue(ex.getMessage().contains("disponivel"));
    }

    @Test
    void deveRealizarEmprestimoComSucessoQuandoRegrasAtendidas() {
        Aluno aluno = new Aluno(1, "123", "Fulano", "fulano@uel.br", false);
        when(alunoDAO.findByRa("123")).thenReturn(Optional.of(aluno));

        Exemplar exemplar = new Exemplar(10, 5, "T-001", Exemplar.Situacao.DISPONIVEL);
        when(exemplarDAO.findById(10)).thenReturn(Optional.of(exemplar));

        Titulo titulo = new Titulo(5, "Clean Code", "Robert Martin", "111", 7);
        when(tituloDAO.findById(5)).thenReturn(Optional.of(titulo));

        // Simula o banco atribuindo um id ao salvar o emprestimo
        when(emprestimoDAO.save(any(Emprestimo.class))).thenAnswer(invocation -> {
            Emprestimo e = invocation.getArgument(0);
            e.setId(99);
            return e;
        });
        when(itemDAO.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Emprestimo resultado = service.emprestar("123", List.of(10));

        assertEquals(99, resultado.getId());
        assertEquals(1, resultado.getItens().size());
        assertEquals(LocalDate.now().plusDays(7), resultado.getItens().get(0).getDataDevolucaoPrevista());

        verify(exemplarDAO).updateSituacao(10, Exemplar.Situacao.EMPRESTADO);
    }

    @Test
    void deveEstenderPrazoQuandoMaisDeDoisExemplaresForemEmprestados() {
        Aluno aluno = new Aluno(1, "123", "Fulano", "fulano@uel.br", false);
        when(alunoDAO.findByRa("123")).thenReturn(Optional.of(aluno));

        Titulo titulo = new Titulo(5, "Clean Code", "Robert Martin", "111", 7);
        when(tituloDAO.findById(5)).thenReturn(Optional.of(titulo));

        for (int id : List.of(10, 11, 12)) {
            Exemplar exemplar = new Exemplar(id, 5, "T-00" + id, Exemplar.Situacao.DISPONIVEL);
            when(exemplarDAO.findById(id)).thenReturn(Optional.of(exemplar));
        }

        when(emprestimoDAO.save(any(Emprestimo.class))).thenAnswer(invocation -> {
            Emprestimo e = invocation.getArgument(0);
            e.setId(100);
            return e;
        });
        when(itemDAO.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Emprestimo resultado = service.emprestar("123", List.of(10, 11, 12));

        // 3 exemplares -> 1 excedente acima de 2 -> +2 dias sobre o prazo base de 7 dias
        LocalDate esperado = LocalDate.now().plusDays(7).plusDays(2);
        assertEquals(esperado, resultado.getItens().get(0).getDataDevolucaoPrevista());
    }
}
