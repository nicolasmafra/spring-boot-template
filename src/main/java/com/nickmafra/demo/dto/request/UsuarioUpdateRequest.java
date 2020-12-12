package com.nickmafra.demo.dto.request;

import com.nickmafra.demo.Messages_;
import com.nickmafra.demo.model.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UsuarioUpdateRequest {

    @NotBlank(message = "{" + Messages_.NOT_BLANK_NOME + "}")
    private String nome;
    private String sobrenome;
    @Past(message = "{" + Messages_.PAST_DATA_NASCIMENTO + "}")
    private LocalDate dataNascimento;

    public Usuario atualizarCampos(Usuario usuario) {
        usuario.setNome(getNome());
        usuario.setSobrenome(getSobrenome());
        usuario.setDataNascimento(getDataNascimento());
        return usuario;
    }
}
