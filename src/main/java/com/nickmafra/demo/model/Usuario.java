package com.nickmafra.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Usuario {

    private String nome;
    private String sobrenome;
    private LocalDate dataNascimento;
}
