package com.biblioteca.controller;

import com.biblioteca.model.Aluno;
import com.biblioteca.service.AlunoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/alunos")
public class AlunoController {

    private final AlunoService alunoService;

    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("alunos", alunoService.listarTodos());
        return "alunos";
    }

    @PostMapping
    public String cadastrar(@RequestParam String ra,
                             @RequestParam String nome,
                             @RequestParam(required = false) String email) {
        alunoService.cadastrar(new Aluno(ra, nome, email));
        return "redirect:/alunos";
    }
}
