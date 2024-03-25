package com.riquelme.javabase.validations;

import org.springframework.beans.factory.annotation.Autowired;

import com.riquelme.javabase.http.HttpRequestOperationStrategy;
import com.riquelme.javabase.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ExistsByEmailValidation implements ConstraintValidator<ExistsByEmail, String> {

    @Autowired
    private UserService service;

    @Autowired
    private HttpRequestOperationStrategy httpRequestOperationStrategy;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (service == null) {
            return true;
        }

        if (httpRequestOperationStrategy.isUpdateOperation()) {
            return true;
        }

        return !service.existsByEmail(email);
    }
}
