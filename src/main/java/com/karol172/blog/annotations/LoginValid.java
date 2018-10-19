package com.karol172.blog.annotations;

import com.karol172.blog.validator.LoginValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LoginValidator.class)
public @interface LoginValid {
    String message() default "Login musi zaczynać się od litery.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

