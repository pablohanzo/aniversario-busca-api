package com.example.demo.controller;

import com.example.demo.service.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Controller
public class FormController {

    private final ColaboradorService colaboradorService;

    @Autowired
    public FormController(ColaboradorService colaboradorService) {
        this.colaboradorService = colaboradorService;
    }

    @PostMapping("/buscar")
    public String buscarAniversariantes(@RequestParam("data") String data, Model model) {
        try {
            // Parse da data no formato DD/MM/YYYY
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dataConsulta = LocalDate.parse(data, formatter);

            // Busca aniversariantes
            String resultado = colaboradorService.verificarAniversario(dataConsulta);

            // Adiciona o resultado ao modelo
            model.addAttribute("resultado", resultado);
            model.addAttribute("dataAtual", data);

            return "index";
        } catch (DateTimeParseException e) {
            model.addAttribute("erro", "Data inválida. Use o formato DD/MM/YYYY");
            return "index";
        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao processar a consulta: " + e.getMessage());
            return "index";
        }
    }
}