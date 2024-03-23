package com.riquelme.springbootcrudhibernaterestful.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponseImpl;
import com.riquelme.springbootcrudhibernaterestful.util.LoggerUtil;

@RestControllerAdvice
public class GlobalHandlerException {

    private final MessageSource messageSource;

    public GlobalHandlerException(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleAllExceptions(Exception ex, WebRequest request) {
        LoggerUtil.error(ex.getMessage(), ex);
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        String messageKey = "general.error.message";

        // Aquí se revisa directamente la excepción y se cambia el estado HTTP a
        // BAD_REQUEST
        if (ex instanceof IllegalArgumentException) {
            statusCode = HttpStatus.BAD_REQUEST;
            messageKey = "user.notContentRole.message";
        }

        if (ex instanceof DataIntegrityViolationException) {
            statusCode = HttpStatus.BAD_REQUEST;
            messageKey = "role.constraintViolation.message";
        }
        return new ResponseEntity<>(new MessageResponseImpl(messageSource, messageKey, null, null),
                statusCode);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {
        return new ResponseEntity<>(new MessageResponseImpl(messageSource, ex.getResourceType(), null, null),
                HttpStatus.NOT_FOUND);
    }
}
