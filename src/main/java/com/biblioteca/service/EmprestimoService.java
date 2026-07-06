package com.biblioteca.service;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servico responsavel pelas regras de negocio do Caso de Uso "Emprestar Livro".
 * Equivalente, em responsabilidade, a classe Controle do esqueleto original,
 * porem delegando persistencia inteiramente aos DAOs.
 */
@Service
public class EmprestimoService {

    private final AlunoDAO alunoDAO;
    private final ExemplarDAO exemplarDAO;
    private final TituloDAO tituloDAO;
    private final EmprestimoDAO emprestimoDAO;
    private final ItemDAO itemDAO;

    public EmprestimoService(AlunoDAO alunoDAO, ExemplarDAO exemplarDAO, TituloDAO tituloDAO,
                              EmprestimoDAO emprestimoDAO, ItemDAO itemDAO) {
        this.alunoDAO = alunoDAO;
        this.exemplarDAO = exemplarDAO;
        this.tituloDAO = tituloDAO;
        this.emprestimoDAO = emprestimoDAO;
        this.itemDAO = itemDAO;
    }

    /**
     * Realiza o emprestimo de um conjunto de exemplares para um aluno.
     *
     * Regras aplicadas (equivalentes as do esqueleto original Controle/Aluno):
     *  1. O aluno precisa existir (buscado pelo RA).
     *  2. O aluno nao pode estar em situacao de debito.
     *  3. Cada exemplar solicitado precisa existir e estar disponivel.
     *  4. A data de devolucao e calculada a partir do prazo do titulo de cada
     *     exemplar; caso o aluno leve mais de 2 exemplares, o prazo final e
     *     estendido em 2 dias para cada exemplar excedente.
     */
    public Emprestimo emprestar(String ra, List<Integer> exemplarIds) {
        Aluno aluno = alunoDAO.findByRa(ra)
                .orElseThrow(() -> new RegraNegocioException("Aluno inexistente: RA " + ra));

        if (aluno.isEmDebito()) {
            throw new RegraNegocioException("Aluno em debito. Emprestimo nao autorizado.");
        }

        if (exemplarIds == null || exemplarIds.isEmpty()) {
            throw new RegraNegocioException("Nenhum exemplar informado para o emprestimo.");
        }

        List<Exemplar> exemplares = new ArrayList<>();
        for (Integer exemplarId : exemplarIds) {
            Exemplar exemplar = exemplarDAO.findById(exemplarId)
                    .orElseThrow(() -> new RegraNegocioException("Exemplar inexistente: id " + exemplarId));
            if (!exemplar.isDisponivel()) {
                throw new RegraNegocioException(
                        "Exemplar " + exemplar.getCodigoTombo() + " nao esta disponivel para emprestimo.");
            }
            exemplares.add(exemplar);
        }

        LocalDate hoje = LocalDate.now();
        LocalDate dataPrevista = calcularDataDevolucao(exemplares, hoje);

        Emprestimo emprestimo = new Emprestimo(aluno.getId(), hoje);
        emprestimo = emprestimoDAO.save(emprestimo);

        for (Exemplar exemplar : exemplares) {
            exemplarDAO.updateSituacao(exemplar.getId(), Exemplar.Situacao.EMPRESTADO);
            Item item = new Item(exemplar.getId(), dataPrevista);
            item.setEmprestimoId(emprestimo.getId());
            item = itemDAO.save(item);
            emprestimo.adicionarItem(item);
        }

        return emprestimo;
    }

    /**
     * Calcula a data de devolucao prevista do emprestimo, com base no maior
     * prazo dentre os titulos dos exemplares, estendendo o prazo quando o
     * aluno leva mais de 2 exemplares no mesmo emprestimo.
     */
    private LocalDate calcularDataDevolucao(List<Exemplar> exemplares, LocalDate dataEmprestimo) {
        LocalDate dataPrevista = dataEmprestimo;
        for (Exemplar exemplar : exemplares) {
            Titulo titulo = tituloDAO.findById(exemplar.getTituloId())
                    .orElseThrow(() -> new RegraNegocioException("Titulo do exemplar nao encontrado."));
            LocalDate candidata = dataEmprestimo.plusDays(titulo.getPrazoEmprestimoDias());
            if (candidata.isAfter(dataPrevista)) {
                dataPrevista = candidata;
            }
        }
        if (exemplares.size() > 2) {
            int excedente = exemplares.size() - 2;
            dataPrevista = dataPrevista.plusDays(excedente * 2L);
        }
        return dataPrevista;
    }
}
