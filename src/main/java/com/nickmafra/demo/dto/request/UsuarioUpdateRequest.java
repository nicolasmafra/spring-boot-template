package com.nickmafra.demo.dto.request;

import com.nickmafra.demo.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UsuarioUpdateRequest {

    @NotBlank(message = "{not-blank.nome}")
    private String nome;
    private String sobrenome;
    @Past(message = "{past.data-nascimento}")
    private LocalDate dataNascimento;

    public Usuario atualizarCampos(Usuario usuario) {
        usuario.setNome(getNome());
        usuario.setSobrenome(getSobrenome());
        usuario.setDataNascimento(getDataNascimento());
        return usuario;
    }
}
