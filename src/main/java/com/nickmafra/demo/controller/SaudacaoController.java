package com.nickmafra.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/saudacao")
public class SaudacaoController {

    @GetMapping
    public String get(@RequestParam(defaultValue = "mundo") String nome) {
        return "Olá, " + nome + "!";
    }
}
