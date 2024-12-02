package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class ColaboradorDTO {
    @JsonProperty("nomFun")
    private String nome;

    @JsonProperty("datNas")
    private LocalDateTime dataNascimento;

    public Colaborador toColaborador() {
        Colaborador colaborador = new Colaborador();
        colaborador.setNome(this.nome);
        colaborador.setDataNascimento(this.dataNascimento);
        return colaborador;
    }

    // Getters and setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDateTime dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}