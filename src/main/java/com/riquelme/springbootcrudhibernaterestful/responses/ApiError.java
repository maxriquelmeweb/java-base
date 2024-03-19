package com.riquelme.springbootcrudhibernaterestful.responses;

import java.util.ArrayList;
import java.util.List;

public class ApiError {
    private List<String> errors;

    public ApiError() {
        this.errors = new ArrayList<>();
    }

    public void addError(String error) {
        this.errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
