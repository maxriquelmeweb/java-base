package com.riquelme.springbootcrudhibernaterestful.controllers;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

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
        result.getFieldErrors().forEach(error -> apiError.addError(error.getDefaultMessage()));
        return ResponseEntity.badRequest()
                .body(new MessageResponseImpl(messageSource, "handleValidationErrors.fails", apiError.getErrors(),
                        null));
    }
}
