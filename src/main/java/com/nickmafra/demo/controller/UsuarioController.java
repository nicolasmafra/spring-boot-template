package com.nickmafra.demo.controller;

import com.nickmafra.demo.model.Usuario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @GetMapping
    public Usuario get(@RequestParam(defaultValue = "Fulano") String nome,
                       @RequestParam(defaultValue = "Mafra") String sobrenome) {

        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setSobrenome(sobrenome);
        usuario.setDataNascimento(LocalDate.of(1997, 7, 29));
        return usuario;
    }
}
