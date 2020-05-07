package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.dto.UsuarioDto;
import com.nickmafra.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public PaginaDto<UsuarioDto> getAll(ConsultaDto consulta) {
        return usuarioService.listar(consulta);
    }

    @GetMapping("/{id}")
    public UsuarioDto getOne(@PathVariable Long id) {
        return usuarioService.encontrar(id);
    }

    @PostMapping
    public Long post(@RequestBody UsuarioDto usuarioDto) {
        return usuarioService.criar(usuarioDto);
    }

    @PutMapping("/{id}")
    public void put(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        usuarioService.atualizar(id, usuarioDto);
    }

    @PostMapping("/{id}")
    public Boolean verificarSenha(@PathVariable Long id, String senha) {
        return usuarioService.verificarSenha(id, senha);
    }
}
