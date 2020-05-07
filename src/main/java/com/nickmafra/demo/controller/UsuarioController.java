package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.dto.UsuarioDto;
import com.nickmafra.demo.exception.BadRequestException;
import com.nickmafra.demo.model.Usuario;
import com.nickmafra.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public PaginaDto<UsuarioDto> getAll(ConsultaDto consulta) {
        Page<Usuario> pageUsuarios = repository.findAll(consulta.toPageable());
        return new PaginaDto<>(pageUsuarios.map(UsuarioDto::new));
    }

    @GetMapping("/{id}")
    public UsuarioDto getOne(@PathVariable Long id) {
        Optional<Usuario> optUsuario = repository.findById(id);
        return optUsuario
                .map(UsuarioDto::new)
                .orElseThrow(() -> new BadRequestException(BadRequestException.MSG_ID_NAO_ENCONTRADO));
    }

    @PostMapping
    public Long post(@RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = usuarioDto.toUsuario();
        return repository.save(usuario).getId();
    }

    @PutMapping("/{id}")
    public void put(@PathVariable Long id, @RequestBody UsuarioDto usuarioDto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new BadRequestException(BadRequestException.MSG_ID_NAO_ENCONTRADO));
        usuarioDto.atualizarUsuario(usuario);
        repository.save(usuario);
    }

    @PostMapping("/{id}")
    public Boolean verificarSenha(@PathVariable Long id, String senha) {
        Optional<Usuario> optUsuario = repository.findById(id);
        return optUsuario.filter(usuario -> usuario.getSenha().equals(senha)).isPresent();
    }
}
