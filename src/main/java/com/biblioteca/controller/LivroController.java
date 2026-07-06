package com.biblioteca.controller;

import com.biblioteca.model.Titulo;
import com.biblioteca.service.CatalogoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/livros")
public class LivroController {

    private final CatalogoService catalogoService;

    public LivroController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("titulos", catalogoService.listarTitulos());
        model.addAttribute("exemplares", catalogoService.listarExemplares());
        model.addAttribute("novoTitulo", new Titulo());
        return "livros";
    }

    @PostMapping("/titulo")
    public String cadastrarTitulo(@RequestParam String nome,
                                   @RequestParam String autor,
                                   @RequestParam(required = false) String isbn,
                                   @RequestParam(defaultValue = "7") int prazoEmprestimoDias) {
        catalogoService.cadastrarTitulo(new Titulo(nome, autor, isbn, prazoEmprestimoDias));
        return "redirect:/livros";
    }

    @PostMapping("/exemplar")
    public String cadastrarExemplar(@RequestParam Integer tituloId,
                                     @RequestParam String codigoTombo) {
        catalogoService.cadastrarExemplar(tituloId, codigoTombo);
        return "redirect:/livros";
    }
}
