package com.nickmafra.demo.service;

import com.nickmafra.demo.dto.LoginDto;
import com.nickmafra.demo.dto.PaginaDto;
import com.nickmafra.demo.dto.UsuarioConsultaDto;
import com.nickmafra.demo.dto.UsuarioDto;
import com.nickmafra.demo.dto.request.UsuarioCreateRequest;
import com.nickmafra.demo.dto.request.UsuarioUpdateRequest;
import com.nickmafra.demo.infra.exception.AppAuthenticationException;
import com.nickmafra.demo.infra.exception.AppRuntimeException;
import com.nickmafra.demo.infra.exception.BadRequestException;
import com.nickmafra.demo.infra.exception.JaCadastradoException;
import com.nickmafra.demo.infra.security.Papel;
import com.nickmafra.demo.model.Usuario;
import com.nickmafra.demo.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

import static com.nickmafra.demo.infra.security.JwtAuthenticationFilter.AUTH_PREFIX;

@Slf4j
@Service
public class UsuarioService {

    public static final String ADMIN_LOGIN = "admin";

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private CriptoService criptoService;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private Environment env;

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
        usuario.setPapel(Papel.COMUM);
        return repository.save(usuario);
    }

    @Transactional
    public Usuario criarAdmin() {
        return repository.findByLogin(ADMIN_LOGIN).orElseGet(() -> {
            Usuario usuario = new Usuario();
            usuario.setNome("Usuário Admin");
            usuario.setLogin(ADMIN_LOGIN);
            String senha = env.getProperty("nickmafra.admin.password"); // assim por segurança
            if (senha == null) {
                throw new AppRuntimeException("Nenhuma senha fornecida para o usuário admin.");
            }
            usuario.setHashSenha(criptoService.ofuscarSenha(senha));
            usuario.setPapel(Papel.ADMIN);
            return repository.save(usuario);
        });
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

    public String realizarLogin(LoginDto loginDto) {
        return buscarPorLoginSenha(loginDto.getLogin(), loginDto.getSenha())
                .map(usuario -> {

                    log.info("Usuário {} realizou login.", loginDto.getLogin());
                    return AUTH_PREFIX + jwtService.gerarToken(usuario);

                }).orElseThrow(() -> new AppAuthenticationException("Usuário ou senha inválidos."));
    }
}
