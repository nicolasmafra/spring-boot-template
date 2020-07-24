package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.*;
import com.nickmafra.demo.dto.request.UsuarioCreateRequest;
import com.nickmafra.demo.dto.request.UsuarioUpdateRequest;
import com.nickmafra.demo.model.Usuario;
import com.nickmafra.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public PaginaDto<UsuarioDto> getAll(ConsultaDto consultaDto) {
        Pageable pageable = consultaDto.toPageable();
        Page<Usuario> pageUsuarios = usuarioService.listar(pageable);
        return new PaginaDto<>(pageUsuarios).map(UsuarioDto::new);
    }

    @GetMapping("/{id}")
    public UsuarioDto getOne(@PathVariable Long id) {
        Usuario usuario = usuarioService.encontrar(id);
        return new UsuarioDto(usuario);
    }

    @PostMapping
    public Long post(@Valid @RequestBody UsuarioCreateRequest request) {
        return usuarioService.criar(request);
    }

    @PutMapping("/{id}")
    public void put(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
        usuarioService.atualizar(id, request);
    }

}
