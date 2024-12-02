package com.example.demo.controller;

import com.example.demo.model.Colaborador;
import com.example.demo.service.ColaboradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;

@RestController
public class WebController {

    @Autowired
    private ColaboradorService colaboradorService;

    @GetMapping("/consultar-aniversariantes")
    public String consultarAniversariantes(@RequestParam("dia") int dia, @RequestParam("mes") int mes) {
        try {
            // Cria um LocalDate com o ano atual, dia e mês fornecidos
            LocalDate dataConsulta = LocalDate.of(LocalDate.now().getYear(), mes, dia);

            // Chama o serviço para buscar aniversariantes
            List<Colaborador> aniversariantes = colaboradorService.buscarAniversariantes(dataConsulta);

            // Verifica se encontrou aniversariantes
            if (aniversariantes.isEmpty()) {
                return "Nenhum aniversariante encontrado para o dia " + dataConsulta.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM"));
            }

            // Cria uma mensagem para cada aniversariante encontrado
            StringBuilder mensagem = new StringBuilder();
            for (Colaborador colaborador : aniversariantes) {
                boolean aniversarioHoje = colaborador.getDataNascimento().getDayOfMonth() == dia && colaborador.getDataNascimento().getMonthValue() == mes;
                mensagem.append(formatarMensagem(colaborador, aniversarioHoje)).append("\n");
            }

            return mensagem.toString();
        } catch (Exception e) {
            return "Erro ao processar os dados: " + e.getMessage(); // Caso ocorra algum erro, retorna uma mensagem de erro
        }
    }

    private String formatarMensagem(Colaborador colaborador, boolean aniversarioHoje) {
        return aniversarioHoje
                ? String.format("Parabéns %s pelo seu aniversário!", colaborador.getNome())
                : String.format("%s, aniversário no dia %02d/%02d", colaborador.getNome(), colaborador.getDataNascimento().getDayOfMonth(), colaborador.getDataNascimento().getMonthValue());
    }
}