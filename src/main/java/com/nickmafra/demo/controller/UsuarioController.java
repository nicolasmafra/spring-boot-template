package com.nickmafra.demo.controller;

import com.nickmafra.demo.model.Usuario;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    private static final Usuario USUARIO = new Usuario("Nicolas", "Mafra", LocalDate.of(1997, 7, 29));
    private List<Usuario> usuarios = new ArrayList<>(Collections.singleton(USUARIO));

    @GetMapping
    public List<Usuario> getAll() {
        return usuarios;
    }

    @PostMapping
    public Usuario post(@RequestBody Usuario usuario) {
        usuarios.add(usuario);
        return usuario;
    }
}
