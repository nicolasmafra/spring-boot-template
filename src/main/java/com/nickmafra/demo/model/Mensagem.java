package com.nickmafra.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Mensagem {

    @GeneratedValue
    @Id
    private Long id;
    private String titulo;
    private String conteudo;

    public Mensagem() {
    }

    public Mensagem(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }
}
