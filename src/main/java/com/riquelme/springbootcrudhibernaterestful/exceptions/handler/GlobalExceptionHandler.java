package com.riquelme.springbootcrudhibernaterestful.exceptions.handler;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.riquelme.springbootcrudhibernaterestful.exceptions.BaseException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.DefaultModificationException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ExistsException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.NotAssignedException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.NotFoundException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.jwt.CustomDeserializationException;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponseImpl;
import com.riquelme.springbootcrudhibernaterestful.util.LoggerUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;
    private final String DEFAULT_MESSAGE_ERROR = "general.error.message";

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private static final Map<Class<? extends Throwable>, HttpStatus> exceptionResponses = new HashMap<>();

    static {
        exceptionResponses.put(NotFoundException.class, HttpStatus.NOT_FOUND);
        exceptionResponses.put(ExistsException.class, HttpStatus.BAD_REQUEST);
        exceptionResponses.put(DefaultModificationException.class, HttpStatus.FORBIDDEN);
        exceptionResponses.put(NotAssignedException.class, HttpStatus.BAD_REQUEST);
        exceptionResponses.put(CustomDeserializationException.class, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleException(Exception ex, Locale locale) {
        HttpStatus status = exceptionResponses.getOrDefault(ex.getClass(), HttpStatus.INTERNAL_SERVER_ERROR);
        String messageKey = ex instanceof BaseException ? ((BaseException) ex).getMessageKey() : DEFAULT_MESSAGE_ERROR;
        MessageResponseImpl messageResponse = new MessageResponseImpl(messageSource, messageKey);
        LoggerUtil.error(messageResponse.getMessage(), ex);
        return new ResponseEntity<>(messageResponse, status);
    }
}
