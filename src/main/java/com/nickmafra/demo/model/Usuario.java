package com.nickmafra.demo.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;

@Data
@Entity
public class Usuario {

    @GeneratedValue
    @Id
    private Long id;
    private String nome;
    private String sobrenome;
    private LocalDate dataNascimento;
    private String login;
    @Transient
    private String senha;
    private String hashSenha;
}
