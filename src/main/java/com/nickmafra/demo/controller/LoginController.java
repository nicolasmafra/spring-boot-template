package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.LoginDto;
import com.nickmafra.demo.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping
@Slf4j
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginDto loginDto) {
        return usuarioService.realizarLogin(loginDto);
    }

}
