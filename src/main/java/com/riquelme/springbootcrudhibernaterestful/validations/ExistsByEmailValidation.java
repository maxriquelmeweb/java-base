package com.riquelme.springbootcrudhibernaterestful.validations;

import org.springframework.beans.factory.annotation.Autowired;

import com.riquelme.springbootcrudhibernaterestful.http.HttpRequestOperationStrategy;
import com.riquelme.springbootcrudhibernaterestful.services.UserService;

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
