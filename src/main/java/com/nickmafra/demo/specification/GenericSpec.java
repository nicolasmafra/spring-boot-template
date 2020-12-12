package com.nickmafra.demo.specification;

import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.Function;

public class GenericSpec {

    private GenericSpec() {}

    public static <T> java.util.function.Predicate<T> negate(java.util.function.Predicate<T> predicate) {
        return predicate == null ? null : predicate.negate();
    }

    @SafeVarargs
    public static <T> java.util.function.Predicate<T> and(java.util.function.Predicate<T>... predicates) {
        if (predicates == null || predicates.length == 0)
            return null;

        java.util.function.Predicate<T> result = null;
        for (java.util.function.Predicate<T> predicate : predicates) {
            if (result == null)
                result = predicate;
            else if (predicate != null)
                result = result.and(predicate);
        }
        return result;
    }

    @SafeVarargs
    public static <T> java.util.function.Predicate<T> or(java.util.function.Predicate<T>... predicates) {
        if (predicates == null || predicates.length == 0)
            return null;

        java.util.function.Predicate<T> result = null;
        for (java.util.function.Predicate<T> predicate : predicates) {
            if (result == null)
                result = predicate;
            else if (predicate != null)
                result = result.or(predicate);
        }
        return result;
    }

    public static <T, U> java.util.function.Predicate<T> isEqualTo(Function<T, U> accessor, U value) {
        return value == null ? null : t -> value.equals(accessor.apply(t));
    }

    public static Predicate literalIsNull(CriteriaBuilder cb, Object obj) {
        // mantém como parâmetro para performar no banco,
        // uma vez que a estrutura da query sempre será a mesma
        return cb.equal(cb.nullLiteral(Object.class), obj);
    }

    private static String toLikeExp(String str) {
        return str == null ? null : ("%" + str.toUpperCase() + "%");
    }

    public static <T> java.util.function.Predicate<T> like(Function<T, String> accessor, String like) {
        return StringUtils.isEmpty(like) ? null
                : t -> accessor.apply(t) != null && accessor.apply(t).toUpperCase().contains(like.toUpperCase());
    }

    public static Predicate like(CriteriaBuilder cb, Path<String> path, String like) {
        return cb.or(literalIsNull(cb, like),
                cb.like(cb.upper(path), toLikeExp(like))
        );
    }

    public static <T> java.util.function.Predicate<T> notBefore(Function<T, LocalDate> accessor, LocalDate date) {
        return date == null ? null : t -> accessor.apply(t) != null && !accessor.apply(t).isBefore(date);
    }

    public static <T> java.util.function.Predicate<T> notAfter(Function<T, LocalDate> accessor, LocalDate date) {
        return date == null ? null : t -> accessor.apply(t) != null && accessor.apply(t).isBefore(date);
    }

    public static <T> java.util.function.Predicate<T> between(Function<T, LocalDate> accessor, LocalDate min, LocalDate max) {
        return and(notBefore(accessor, min), notAfter(accessor, max));
    }

    public static <T> java.util.function.Predicate<T> notBefore(Function<T, LocalDateTime> accessor, LocalDateTime date) {
        return date == null ? null : t -> accessor.apply(t) != null && !accessor.apply(t).isBefore(date);
    }

    public static <T> java.util.function.Predicate<T> notAfter(Function<T, LocalDateTime> accessor, LocalDateTime date) {
        return date == null ? null : t -> accessor.apply(t) != null && accessor.apply(t).isBefore(date);
    }

    public static <T> java.util.function.Predicate<T> between(Function<T, LocalDateTime> accessor, LocalDateTime min, LocalDateTime max) {
        return and(notBefore(accessor, min), notAfter(accessor, max));
    }
}
