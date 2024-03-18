package com.riquelme.springbootcrudhibernaterestful.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistsByEmailValidation.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByEmail {

    String message() default "El correo electrónico ya está en uso";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
