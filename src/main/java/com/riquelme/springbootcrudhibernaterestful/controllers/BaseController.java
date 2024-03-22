package com.riquelme.springbootcrudhibernaterestful.controllers;

import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.riquelme.springbootcrudhibernaterestful.responses.ApiError;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponseImpl;
import com.riquelme.springbootcrudhibernaterestful.util.LoggerUtil;

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
        Map<String, String> details = apiError.getFieldErrors();
        MessageResponseImpl messageResponse = new MessageResponseImpl(messageSource, "handleValidationErrors.fails",
                details,
                null);
        LoggerUtil.debug(messageResponse.getMessage(), details);
        return ResponseEntity.badRequest()
                .body(messageResponse);
    }
}
