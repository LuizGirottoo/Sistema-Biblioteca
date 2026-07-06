package com.biblioteca;

import com.biblioteca.dao.AlunoDAO;
import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.dao.ExemplarDAO;
import com.biblioteca.dao.ItemDAO;
import com.biblioteca.exception.RegraNegocioException;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Item;
import com.biblioteca.service.DevolucaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Testes unitarios do Caso de Uso "Devolver Livro".
 */
class DevolucaoServiceTest {

    private ItemDAO itemDAO;
    private ExemplarDAO exemplarDAO;
    private EmprestimoDAO emprestimoDAO;
    private AlunoDAO alunoDAO;
    private DevolucaoService service;

    @BeforeEach
    void setUp() {
        itemDAO = mock(ItemDAO.class);
        exemplarDAO = mock(ExemplarDAO.class);
        emprestimoDAO = mock(EmprestimoDAO.class);
        alunoDAO = mock(AlunoDAO.class);
        service = new DevolucaoService(itemDAO, exemplarDAO, emprestimoDAO, alunoDAO);
    }

    @Test
    void deveRecusarDevolucaoDeItemInexistente() {
        when(itemDAO.findById(1)).thenReturn(Optional.empty());
        assertThrows(RegraNegocioException.class, () -> service.devolver(1));
    }

    @Test
    void deveRecusarDevolucaoDeItemJaDevolvido() {
        Item item = new Item(1, 50, 10, LocalDate.now().plusDays(3), LocalDate.now());
        when(itemDAO.findById(1)).thenReturn(Optional.of(item));

        assertThrows(RegraNegocioException.class, () -> service.devolver(1));
    }

    @Test
    void deveRegistrarDevolucaoDentroDoPrazoSemMarcarDebito() {
        Item item = new Item(1, 50, 10, LocalDate.now().plusDays(3), null);
        when(itemDAO.findById(1)).thenReturn(Optional.of(item));

        Emprestimo emprestimo = new Emprestimo(50, 7, LocalDate.now().minusDays(1), Emprestimo.Status.ATIVO);
        when(emprestimoDAO.findById(50)).thenReturn(Optional.of(emprestimo));
        when(itemDAO.findByEmprestimoId(50)).thenReturn(List.of(
                new Item(1, 50, 10, LocalDate.now().plusDays(3), LocalDate.now())
        ));

        boolean noPrazo = service.devolver(1);

        assertTrue(noPrazo);
        verify(exemplarDAO).updateSituacao(10, Exemplar.Situacao.DISPONIVEL);
        verify(alunoDAO, never()).updateDebito(anyInt(), anyBoolean());
        verify(emprestimoDAO).atualizarStatus(50, Emprestimo.Status.FINALIZADO);
    }

    @Test
    void deveMarcarAlunoEmDebitoQuandoDevolucaoEstiverAtrasada() {
        Item item = new Item(2, 51, 11, LocalDate.now().minusDays(2), null);
        when(itemDAO.findById(2)).thenReturn(Optional.of(item));

        Emprestimo emprestimo = new Emprestimo(51, 7, LocalDate.now().minusDays(10), Emprestimo.Status.ATIVO);
        when(emprestimoDAO.findById(51)).thenReturn(Optional.of(emprestimo));
        when(itemDAO.findByEmprestimoId(51)).thenReturn(List.of(
                new Item(2, 51, 11, LocalDate.now().minusDays(2), LocalDate.now())
        ));

        boolean noPrazo = service.devolver(2);

        assertFalse(noPrazo);
        verify(alunoDAO).updateDebito(7, true);
    }
}
