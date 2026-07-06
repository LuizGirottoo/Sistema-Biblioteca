package com.biblioteca.controller;

import com.biblioteca.exception.RegraNegocioException;
import com.biblioteca.model.Emprestimo;
import com.biblioteca.model.Exemplar;
import com.biblioteca.model.Titulo;
import com.biblioteca.service.CatalogoService;
import com.biblioteca.service.EmprestimoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/emprestar")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;
    private final CatalogoService catalogoService;

    public EmprestimoController(EmprestimoService emprestimoService, CatalogoService catalogoService) {
        this.emprestimoService = emprestimoService;
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public String form(Model model) {
        model.addAttribute("titulos", catalogoService.listarTitulos());
        model.addAttribute("exemplares", catalogoService.listarExemplares());
        return "emprestar";
    }

    @PostMapping
    public String emprestar(@RequestParam String ra,
                             @RequestParam(name = "exemplarIds") List<Integer> exemplarIds,
                             Model model) {
        try {
            Emprestimo emprestimo = emprestimoService.emprestar(ra, exemplarIds);
            model.addAttribute("mensagemSucesso",
                    "Emprestimo #" + emprestimo.getId() + " realizado com sucesso. "
                            + "Data de devolucao prevista para todos os itens: "
                            + emprestimo.getItens().get(0).getDataDevolucaoPrevista());
        } catch (RegraNegocioException e) {
            model.addAttribute("mensagemErro", e.getMessage());
        }
        model.addAttribute("titulos", catalogoService.listarTitulos());
        model.addAttribute("exemplares", catalogoService.listarExemplares());
        return "emprestar";
    }
}
