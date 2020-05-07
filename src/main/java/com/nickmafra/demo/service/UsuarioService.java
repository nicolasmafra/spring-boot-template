package com.nickmafra.demo.service;

import com.nickmafra.demo.dto.ConsultaDto;
import com.nickmafra.demo.dto.LoginDto;
import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.dto.UsuarioDto;
import com.nickmafra.demo.infra.exception.BadRequestException;
import com.nickmafra.demo.infra.exception.JaCadastradoException;
import com.nickmafra.demo.model.Usuario;
import com.nickmafra.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private CriptoService criptoService;

    public PaginaDto<UsuarioDto> listar(ConsultaDto consulta) {
        Page<Usuario> pageUsuarios = repository.findAll(consulta.toPageable());
        return new PaginaDto<>(pageUsuarios).map(UsuarioDto::new);
    }

    public UsuarioDto encontrar(Long id) {
        Optional<Usuario> optUsuario = repository.findById(id);
        return optUsuario
                .map(UsuarioDto::new)
                .orElseThrow(() -> new BadRequestException(BadRequestException.MSG_ID_NAO_ENCONTRADO));
    }

    public void validarNovoUsuario(UsuarioDto usuarioDto) {
        if (StringUtils.isEmpty(usuarioDto.getLogin()) || StringUtils.isEmpty(usuarioDto.getSenha())) {
            throw new JaCadastradoException("Login e senha devem são obrigatórios.");
        }
        if (repository.existsByLogin(usuarioDto.getLogin())) {
            throw new JaCadastradoException("Login já cadastrado.");
        }
        criptoService.validarForcaSenha(usuarioDto.getSenha());
    }

    public Long criar(UsuarioDto usuarioDto) {
        validarNovoUsuario(usuarioDto);
        Usuario usuario = usuarioDto.toUsuario();
        usuario.setSenha(criptoService.ofuscarSenha(usuario.getSenha()));
        return repository.save(usuario).getId();
    }

    public void atualizar(Long id, UsuarioDto usuarioDto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new BadRequestException(BadRequestException.MSG_ID_NAO_ENCONTRADO));
        usuarioDto.atualizarUsuario(usuario);
        repository.save(usuario);
    }

    public Boolean verificarLoginSenha(LoginDto loginDto) {
        return repository.findByLogin(loginDto.getLogin())
                .filter(usuario -> criptoService.conferirSenhaOfuscada(loginDto.getSenha(), usuario.getSenha()))
                .isPresent();
    }
}
