package com.nickmafra.demo.specification;

import com.nickmafra.demo.dto.UsuarioConsultaDto;
import com.nickmafra.demo.model.Usuario;
import com.nickmafra.demo.model.Usuario_;
import org.springframework.data.jpa.domain.Specification;

import static com.nickmafra.demo.specification.GenericSpec.like;

public class UsuarioSpec {

    private UsuarioSpec() {}

    public static Specification<Usuario> loginLike(String likeExp) {
        return (root, cq, cb) -> like(cb, root.get(Usuario_.login), likeExp);
    }

    public static Specification<Usuario> nomeOuSobrenomeLike(String likeExp) {
        return (root, cq, cb) -> cb.or(
                like(cb, root.get(Usuario_.nome), likeExp),
                like(cb, root.get(Usuario_.sobrenome), likeExp)
        );
    }

    public static Specification<Usuario> toSpec(UsuarioConsultaDto consultaDto) {
        return loginLike(consultaDto.getLogin()).and(nomeOuSobrenomeLike(consultaDto.getNome()));
    }
}
