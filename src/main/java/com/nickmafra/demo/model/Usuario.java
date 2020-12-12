package com.nickmafra.demo.model;

import com.nickmafra.demo.infra.security.Papel;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Usuario {

    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private String nome;
    private String sobrenome;
    private LocalDate dataNascimento;

    @Column(nullable = false)
    private String login;

    @Transient
    private String senha;
    @Column(nullable = false)
    private String hashSenha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Papel papel;
}
