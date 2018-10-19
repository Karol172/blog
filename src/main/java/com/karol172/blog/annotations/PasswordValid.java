package com.karol172.blog.annotations;

import com.karol172.blog.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordValid {
    String message() default "Dozwolone znaki: A-Z a-z 0-9 !@#$%^&*()_+-=,.<>/?|{}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
