package com.nickmafra.demo.service;

import com.nickmafra.demo.dto.request.UsuarioCreateRequest;
import com.nickmafra.demo.dto.request.UsuarioUpdateRequest;
import com.nickmafra.demo.infra.exception.BadRequestException;
import com.nickmafra.demo.infra.exception.JaCadastradoException;
import com.nickmafra.demo.model.Usuario;
import com.nickmafra.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private CriptoService criptoService;

    public Page<Usuario> listar(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Usuario encontrar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new BadRequestException(BadRequestException.MSG_ID_NAO_ENCONTRADO));
    }

    public void validarUsuarioNovo(UsuarioCreateRequest request) {
        if (repository.existsByLogin(request.getLogin())) {
            throw new JaCadastradoException("Login já cadastrado.");
        }
    }

    public void validarUsuarioExistente(Usuario usuario, UsuarioUpdateRequest request) {
        // nada por enquanto
    }

    public Long criar(UsuarioCreateRequest request) {
        validarUsuarioNovo(request);
        Usuario usuario = request.atualizarCampos(new Usuario());
        usuario.setHashSenha(criptoService.ofuscarSenha(usuario.getSenha()));
        usuario.setSenha(null); // por segurança
        return repository.save(usuario).getId();
    }

    public void atualizar(Long id, UsuarioUpdateRequest request) {
        Usuario usuario = encontrar(id);
        validarUsuarioExistente(usuario, request);
        request.atualizarCampos(usuario);
        repository.save(usuario);
    }

    public Boolean verificarLoginSenha(String login, String senha) {
        return repository.findByLogin(login)
                .filter(usuario -> criptoService.conferirSenhaOfuscada(senha, usuario.getHashSenha()))
                .isPresent();
    }
}
