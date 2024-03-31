package com.riquelme.javabase.controllers;

import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.riquelme.javabase.responses.ApiError;
import com.riquelme.javabase.responses.MessageResponse;
import com.riquelme.javabase.responses.MessageResponseImpl;
import com.riquelme.javabase.util.LoggerUtil;

abstract class BaseController {

    protected final MessageSource messageSource;

    protected BaseController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    protected ResponseEntity<MessageResponse> handleValidationErrors(BindingResult result) {
        ApiError apiError = new ApiError();
        result.getFieldErrors()
                .forEach(fieldError -> apiError.addFieldError(fieldError.getField(), fieldError.getDefaultMessage()));
        Map<String, String> details = apiError.getFieldErrors();
        MessageResponseImpl messageResponse = new MessageResponseImpl(messageSource, "error.validation",
                details);
        LoggerUtil.debug(messageResponse.getMessage(), details);
        return ResponseEntity.badRequest()
                .body(messageResponse);
    }
}
