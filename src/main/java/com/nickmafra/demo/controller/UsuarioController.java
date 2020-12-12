package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.dto.UsuarioConsultaDto;
import com.nickmafra.demo.dto.UsuarioDto;
import com.nickmafra.demo.dto.request.UsuarioCreateRequest;
import com.nickmafra.demo.dto.request.UsuarioUpdateRequest;
import com.nickmafra.demo.infra.security.Acesso;
import com.nickmafra.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Secured(Acesso.PAPEL_ADMIN)
    @GetMapping
    public PaginaDto<UsuarioDto> getFiltered(UsuarioConsultaDto consultaDto) {
        return usuarioService.listarDto(consultaDto);
    }

    @PreAuthorize(Acesso.ADMIN_OU_PROPRIO_USUARIO)
    @GetMapping("/{id}")
    public UsuarioDto getOne(@PathVariable Long id) {
        return usuarioService.encontrarDto(id);
    }

    @PostMapping
    public UsuarioDto post(@Valid @RequestBody UsuarioCreateRequest request) {
        return usuarioService.criarDto(request);
    }

    @PreAuthorize(Acesso.ADMIN_OU_PROPRIO_USUARIO)
    @PutMapping("/{id}")
    public UsuarioDto put(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
        return usuarioService.atualizarDto(id, request);
    }

}
