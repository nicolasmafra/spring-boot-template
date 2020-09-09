package com.nickmafra.demo.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

public class GenericSpec {

    private GenericSpec() {}

    public static Predicate literalIsNull(CriteriaBuilder cb, Object obj) {
        // mantém como parâmetro para performar no banco,
        // uma vez que a estrutura da query sempre será a mesma
        return cb.equal(cb.nullLiteral(Object.class), obj);
    }

    private static String toLikeExp(String str) {
        return str == null ? null : ("%" + str.toUpperCase() + "%");
    }

    public static Predicate like(CriteriaBuilder cb, Path<String> path, String like) {
        return cb.or(literalIsNull(cb, like),
                cb.like(cb.upper(path), toLikeExp(like))
        );
    }
}
