package com.nickmafra.demo.controller;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.exception.BadRequestException;
import com.nickmafra.demo.model.Usuario;
import com.nickmafra.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public PaginaDto<Usuario> getAll(ConsultaDto consulta) {
        return new PaginaDto<>(repository.findAll(consulta.toPageable()));
    }

    @GetMapping("/{id}")
    public Optional<Usuario> getOne(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PostMapping
    public Usuario post(@RequestBody Usuario usuario) {
        return repository.save(usuario);
    }

    @PutMapping("/{id}")
    public void put(@PathVariable Long id, @RequestBody Usuario usuario) {
        if (!repository.existsById(id)) {
            throw new BadRequestException(BadRequestException.MSG_ID_NAO_ENCONTRADO);
        }
        repository.save(usuario);
    }
}
