package com.nickmafra.demo.controller;

import com.nickmafra.demo.model.Usuario;
import com.nickmafra.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @GetMapping
    public List<Usuario> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Usuario> getOne(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PostMapping
    public Usuario post(@RequestBody Usuario usuario) {
        return repository.save(usuario);
    }
}
