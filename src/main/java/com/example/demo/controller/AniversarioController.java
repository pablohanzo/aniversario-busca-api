package com.example.demo.controller;

import com.example.demo.service.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class AniversarioController {

    private final ColaboradorService colaboradorService;

    @Autowired
    public AniversarioController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @GetMapping("/aniversariantes")
    public String verificarAniversariantes(@RequestParam int dia, @RequestParam int mes) {
        LocalDate data = LocalDate.of(LocalDate.now().getYear(), mes, dia);
        return colaboradorService.verificarAniversario(data);
    }
}