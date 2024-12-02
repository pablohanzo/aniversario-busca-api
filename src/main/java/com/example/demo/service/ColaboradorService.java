package com.example.demo.service;

import com.example.demo.model.Colaborador;
import com.example.demo.model.ColaboradorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ColaboradorService {
    private static final Logger logger = LoggerFactory.getLogger(ColaboradorService.class);
    private final String API_BASE_URL = "https://homolog.seniorconnect.com.br/api/trial-dediccar.demo.net-hmg/consultaapi/sqltestedediccar/consulta-banco-colaboradores/";
    private final RestTemplate restTemplate;

    @Autowired
    public ColaboradorService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Colaborador> buscarAniversariantes(LocalDate data) {
        String urlComParametros = UriComponentsBuilder.fromUriString(API_BASE_URL)
                .queryParam("dia", data.getDayOfMonth())
                .queryParam("mes", data.getMonthValue())
                .build()
                .toUriString();

        logger.info("Chamando API para buscar aniversariantes: {}", urlComParametros);

        try {
            ColaboradorDTO[] response = restTemplate.getForObject(urlComParametros, ColaboradorDTO[].class);
            if (response == null || response.length == 0) {
                logger.warn("Nenhum aniversariante encontrado para a data: {}", data);
                return List.of();
            }

            List<Colaborador> colaboradores = Arrays.stream(response)
                    .map(ColaboradorDTO::toColaborador)
                    .distinct() // Remove duplicates
                    .collect(Collectors.toList());

            logger.info("Encontrados {} aniversariantes", colaboradores.size());
            return colaboradores;

        } catch (Exception e) {
            logger.error("Erro ao buscar aniversariantes: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao consultar aniversariantes: " + e.getMessage());
        }
    }

    public String verificarAniversario(LocalDate dataConsulta) {
        List<Colaborador> aniversariantes = buscarAniversariantes(dataConsulta);

        if (aniversariantes.isEmpty()) {
            return String.format("Nenhum aniversariante encontrado para o dia %02d/%02d",
                    dataConsulta.getDayOfMonth(), dataConsulta.getMonthValue());
        }

        LocalDate hoje = LocalDate.now();
        boolean aniversarioHoje = hoje.equals(dataConsulta);

        StringBuilder resultado = new StringBuilder();
        for (Colaborador colaborador : aniversariantes) {
            resultado.append(formatarMensagem(colaborador, aniversarioHoje)).append("\n");
        }

        return resultado.toString().trim();
    }

    private String formatarMensagem(Colaborador colaborador, boolean aniversarioHoje) {
        return aniversarioHoje
                ? String.format("Parabéns %s pelo seu aniversário!", colaborador.getNome())
                : String.format("%s, aniversário no dia %02d/%02d",
                colaborador.getNome(),
                colaborador.getDataNascimento().getDayOfMonth(),
                colaborador.getDataNascimento().getMonthValue());
    }
}