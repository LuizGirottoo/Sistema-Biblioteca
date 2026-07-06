package com.biblioteca.controller;

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
import com.biblioteca.service.DevolucaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/devolver")
public class DevolucaoController {

    private final DevolucaoService devolucaoService;
    private final ItemDAO itemDAO;
    private final ExemplarDAO exemplarDAO;
    private final TituloDAO tituloDAO;
    private final EmprestimoDAO emprestimoDAO;
    private final AlunoDAO alunoDAO;

    public DevolucaoController(DevolucaoService devolucaoService, ItemDAO itemDAO, ExemplarDAO exemplarDAO,
                                TituloDAO tituloDAO, EmprestimoDAO emprestimoDAO, AlunoDAO alunoDAO) {
        this.devolucaoService = devolucaoService;
        this.itemDAO = itemDAO;
        this.exemplarDAO = exemplarDAO;
        this.tituloDAO = tituloDAO;
        this.emprestimoDAO = emprestimoDAO;
        this.alunoDAO = alunoDAO;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("itensPendentes", montarItensPendentes());
        return "devolver";
    }

    @PostMapping("/{itemId}")
    public String devolver(@PathVariable Integer itemId, Model model) {
        try {
            boolean noPrazo = devolucaoService.devolver(itemId);
            model.addAttribute("mensagemSucesso", noPrazo
                    ? "Devolucao registrada dentro do prazo."
                    : "Devolucao registrada com atraso. Aluno foi marcado em debito.");
        } catch (RegraNegocioException e) {
            model.addAttribute("mensagemErro", e.getMessage());
        }
        model.addAttribute("itensPendentes", montarItensPendentes());
        return "devolver";
    }

    private List<ItemPendenteView> montarItensPendentes() {
        LocalDate hoje = LocalDate.now();
        List<Item> pendentes = itemDAO.findPendentes();
        return pendentes.stream().map(item -> {
            Exemplar exemplar = exemplarDAO.findById(item.getExemplarId()).orElseThrow();
            Titulo titulo = tituloDAO.findById(exemplar.getTituloId()).orElseThrow();
            Emprestimo emprestimo = emprestimoDAO.findById(item.getEmprestimoId()).orElseThrow();
            Aluno aluno = alunoDAO.findById(emprestimo.getAlunoId()).orElseThrow();
            return new ItemPendenteView(
                    item.getId(),
                    exemplar.getCodigoTombo(),
                    titulo.getNome(),
                    aluno.getNome(),
                    aluno.getRa(),
                    item.getDataDevolucaoPrevista(),
                    item.isAtrasado(hoje)
            );
        }).collect(Collectors.toList());
    }
}
