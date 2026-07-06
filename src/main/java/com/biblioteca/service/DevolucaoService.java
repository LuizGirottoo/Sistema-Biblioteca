package com.biblioteca.service;

import com.biblioteca.dao.AlunoDAO;
import com.biblioteca.dao.EmprestimoDAO;
import com.biblioteca.dao.ExemplarDAO;
import com.biblioteca.dao.ItemDAO;
import com.biblioteca.exception.RegraNegocioException;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Item;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Servico responsavel pelas regras de negocio do Caso de Uso "Devolver Livro".
 */
@Service
public class DevolucaoService {

    private final ItemDAO itemDAO;
    private final ExemplarDAO exemplarDAO;
    private final EmprestimoDAO emprestimoDAO;
    private final AlunoDAO alunoDAO;

    public DevolucaoService(ItemDAO itemDAO, ExemplarDAO exemplarDAO,
                             EmprestimoDAO emprestimoDAO, AlunoDAO alunoDAO) {
        this.itemDAO = itemDAO;
        this.exemplarDAO = exemplarDAO;
        this.emprestimoDAO = emprestimoDAO;
        this.alunoDAO = alunoDAO;
    }

    /**
     * Registra a devolucao de um item (exemplar) de um emprestimo.
     *
     * Regras aplicadas:
     *  1. O item precisa existir e ainda nao ter sido devolvido.
     *  2. Ao devolver, o exemplar volta a ficar DISPONIVEL.
     *  3. Se a devolucao ocorrer apos a data prevista, o aluno passa a
     *     ficar em situacao de debito (regra equivalente a classe Debito
     *     do esqueleto original, porem persistida).
     *  4. Quando todos os itens de um emprestimo forem devolvidos, o
     *     emprestimo e marcado como FINALIZADO.
     *
     * @return true se a devolucao ocorreu dentro do prazo, false se houve atraso.
     */
    public boolean devolver(Integer itemId) {
        Item item = itemDAO.findById(itemId)
                .orElseThrow(() -> new RegraNegocioException("Item de emprestimo inexistente: id " + itemId));

        if (item.isDevolvido()) {
            throw new RegraNegocioException("Este item ja foi devolvido anteriormente.");
        }

        LocalDate hoje = LocalDate.now();
        boolean atrasado = item.isAtrasado(hoje);

        itemDAO.registrarDevolucao(itemId, hoje);
        exemplarDAO.updateSituacao(item.getExemplarId(), Exemplar.Situacao.DISPONIVEL);

        Emprestimo emprestimo = emprestimoDAO.findById(item.getEmprestimoId())
                .orElseThrow(() -> new RegraNegocioException("Emprestimo associado nao encontrado."));

        if (atrasado) {
            alunoDAO.updateDebito(emprestimo.getAlunoId(), true);
        }

        finalizarEmprestimoSeCompleto(emprestimo);

        return !atrasado;
    }

    private void finalizarEmprestimoSeCompleto(Emprestimo emprestimo) {
        List<Item> itens = itemDAO.findByEmprestimoId(emprestimo.getId());
        boolean todosDevolvidos = itens.stream().allMatch(Item::isDevolvido);
        if (todosDevolvidos) {
            emprestimoDAO.atualizarStatus(emprestimo.getId(), Emprestimo.Status.FINALIZADO);
        }
    }
}
