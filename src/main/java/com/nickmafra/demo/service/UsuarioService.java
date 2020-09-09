package com.nickmafra.demo.service;

import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.dto.UsuarioConsultaDto;
import com.nickmafra.demo.dto.UsuarioDto;
import com.nickmafra.demo.dto.request.UsuarioCreateRequest;
import com.nickmafra.demo.dto.request.UsuarioUpdateRequest;
import com.nickmafra.demo.infra.exception.BadRequestException;
import com.nickmafra.demo.infra.exception.JaCadastradoException;
import com.nickmafra.demo.model.Usuario;
import com.nickmafra.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private CriptoService criptoService;

    public Page<Usuario> listar(UsuarioConsultaDto consultaDto) {
        return repository.findAll(consultaDto.toSpec(), consultaDto.toPageable());
    }

    public PaginaDto<UsuarioDto> listarDto(UsuarioConsultaDto consultaDto) {
        Page<Usuario> pageUsuarios = listar(consultaDto);
        return new PaginaDto<>(pageUsuarios).map(UsuarioDto::new);
    }

    public Usuario encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException(BadRequestException.MSG_ID_NAO_ENCONTRADO));
    }

    public UsuarioDto encontrarDto(Long id) {
        return new UsuarioDto(encontrar(id));
    }

    public void validarUsuarioNovo(UsuarioCreateRequest request) {
        if (repository.existsByLogin(request.getLogin())) {
            throw new JaCadastradoException("Login já cadastrado.");
        }
    }

    public void validarUsuarioExistente(Usuario usuario, UsuarioUpdateRequest request) {
        // nada por enquanto
    }

    public Usuario criar(UsuarioCreateRequest request) {
        validarUsuarioNovo(request);
        Usuario usuario = request.atualizarCampos(new Usuario());
        usuario.setHashSenha(criptoService.ofuscarSenha(usuario.getSenha()));
        usuario.setSenha(null); // por segurança
        return repository.save(usuario);
    }

    public UsuarioDto criarDto(UsuarioCreateRequest request) {
        return new UsuarioDto(criar(request));
    }

    public Usuario atualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = encontrar(id);
        validarUsuarioExistente(usuario, request);
        request.atualizarCampos(usuario);
        return repository.save(usuario);
    }

    public UsuarioDto atualizarDto(Long id, UsuarioUpdateRequest request) {
        return new UsuarioDto(atualizar(id, request));
    }

    public Optional<Usuario> buscarPorLoginSenha(String login, String senha) {
        return repository.findByLogin(login)
                .filter(usuario -> criptoService.conferirSenhaOfuscada(senha, usuario.getHashSenha()));
    }
}
