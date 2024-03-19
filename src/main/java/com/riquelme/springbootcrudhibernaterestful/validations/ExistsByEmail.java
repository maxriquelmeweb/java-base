package com.riquelme.springbootcrudhibernaterestful.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Value;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistsByEmailValidation.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByEmail {

    @Value("${existsByEmail.message}")
    String message() default "el correo electrónico ya está en uso";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
