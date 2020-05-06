package com.nickmafra.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saudacao")
public class SaudacaoController {

    @GetMapping()
    private String get(String nome) {
        return "Olá, " + (nome == null ? "World" : nome) + "!";
    }
}
