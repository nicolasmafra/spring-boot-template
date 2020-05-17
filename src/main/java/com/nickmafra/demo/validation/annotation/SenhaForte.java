package com.nickmafra.demo.validation.annotation;

import com.nickmafra.demo.validation.SenhaForteValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = { SenhaForteValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface SenhaForte {
    String message() default "{senha.fraca}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
