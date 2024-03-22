package com.riquelme.springbootcrudhibernaterestful.validations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = ExistsByNameRoleValidation.class)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistsByNameRole {

    String message() default "{existsByNameRole.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
