package com.riquelme.springbootcrudhibernaterestful.controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.riquelme.springbootcrudhibernaterestful.responses.ApiError;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponseImpl;

abstract class BaseController {

    protected final MessageSource messageSource;

    protected BaseController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected ResponseEntity<MessageResponse> handleValidationErrors(BindingResult result) {
        ApiError apiError = new ApiError();
        for (FieldError fieldError : result.getFieldErrors()) {
            apiError.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest()
                .body(new MessageResponseImpl(messageSource, "handleValidationErrors.fails", apiError.getFieldErrors(),
                        null));
    }
}
