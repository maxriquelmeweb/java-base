package com.riquelme.javabase.responses;

import java.util.HashMap;
import java.util.Map;

public class ApiError {
    private Map<String, String> fieldErrors;

    public ApiError() {
        this.fieldErrors = new HashMap<>();
    }

    public void addFieldError(String field, String error) {
        this.fieldErrors.put(field, error);
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
