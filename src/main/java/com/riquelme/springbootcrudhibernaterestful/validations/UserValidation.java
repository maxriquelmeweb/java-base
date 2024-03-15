package com.riquelme.springbootcrudhibernaterestful.validations;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.riquelme.springbootcrudhibernaterestful.entities.User;

@Component
public class UserValidation implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        //aqui pueden ir validaciones personalizadas
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

}
