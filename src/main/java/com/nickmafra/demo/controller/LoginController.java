package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.LoginDto;
import com.nickmafra.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public Boolean login(@Valid @RequestBody LoginDto loginDto) {
        // TODO retornar token para posterior autenticação
        return usuarioService.verificarLoginSenha(loginDto.getLogin(), loginDto.getSenha());
    }

}
