package com.riquelme.springbootcrudhibernaterestful.exceptions;

import org.springframework.context.MessageSource;
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
        String messageException = ex.getMessage();
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        String messageKey = "general.error.message";

        /*
         * if (ex instanceof DataIntegrityViolationException) {
         * statusCode = HttpStatus.BAD_REQUEST;
         * messageKey = "role.constraintViolation.message";
         * }
         */

        MessageResponseImpl messageResponseImpl = new MessageResponseImpl(messageSource, messageKey, null, null);
        if (messageException == null) {
            messageException = messageResponseImpl.getMessage();
        }
        LoggerUtil.error(messageException, ex);
        return new ResponseEntity<>(messageResponseImpl, statusCode);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<MessageResponse> GlobalCustomException(CustomException ex,
            WebRequest request) {
        String messageException = ex.getThrowable().getMessage();
        String messageKey = ex.getKeyMessage();
        Throwable throwable = ex.getThrowable();
        HttpStatus statusCode = ex.getStatusCode();
        MessageResponseImpl messageResponseImpl = new MessageResponseImpl(messageSource, messageKey, null, null);
        if (messageException == null) {
            messageException = messageResponseImpl.getMessage();
        }
        LoggerUtil.error(messageException, throwable);
        return new ResponseEntity<>(messageResponseImpl, statusCode);
    }
}
