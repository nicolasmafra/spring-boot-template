package com.nickmafra.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nickmafra.demo.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UsuarioDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    private String nome;
    private String sobrenome;
    private LocalDate dataNascimento;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    public UsuarioDto(Usuario usuario) {
        id = usuario.getId();
        nome = usuario.getNome();
        sobrenome = usuario.getSobrenome();
        dataNascimento = usuario.getDataNascimento();
    }

    /**
     * Cria um usuário com os campos deste DTO.
     *
     * @return um novo usuário
     */
    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        atualizarUsuario(usuario);
        // campos não diretamente atualizáveis
        usuario.setSenha(senha);
        return usuario;
    }

    /**
     * Atualiza o usuário com os campos deste DTO.
     *
     * @param usuario uma instância vazia
     */
    public void atualizarUsuario(Usuario usuario) {
        usuario.setNome(nome);
        usuario.setSobrenome(sobrenome);
        usuario.setDataNascimento(dataNascimento);
    }
}
