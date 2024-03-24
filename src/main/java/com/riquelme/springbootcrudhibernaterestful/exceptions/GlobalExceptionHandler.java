package com.riquelme.springbootcrudhibernaterestful.exceptions;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.riquelme.springbootcrudhibernaterestful.exceptions.login.CustomDeserializationException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.role.DefaultRoleModificationException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.role.RoleNameAlreadyExistsException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.role.RoleNotAssignedToUserException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.role.RoleNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.user.UserEmailExistsException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.user.UserNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.user.UserRoleNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponseImpl;
import com.riquelme.springbootcrudhibernaterestful.util.LoggerUtil;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private static final Map<Class<? extends Throwable>, ExceptionResponse> exceptionResponses = new HashMap<>();

    static {
        exceptionResponses.put(RoleNotFoundException.class, new ExceptionResponse(HttpStatus.NOT_FOUND, "role.notfound.message"));
        exceptionResponses.put(UserNotFoundException.class, new ExceptionResponse(HttpStatus.NOT_FOUND, "user.notfound.message"));
        exceptionResponses.put(UserEmailExistsException.class, new ExceptionResponse(HttpStatus.BAD_REQUEST, "user.email.exists.message"));
        exceptionResponses.put(UserRoleNotFoundException.class, new ExceptionResponse(HttpStatus.NOT_FOUND, "user.role.notfound.message"));
        exceptionResponses.put(RoleNameAlreadyExistsException.class, new ExceptionResponse(HttpStatus.CONFLICT, "role.existsByNameRole.message"));
        exceptionResponses.put(DefaultRoleModificationException.class, new ExceptionResponse(HttpStatus.FORBIDDEN, "role.default.modification.forbidden"));
        exceptionResponses.put(RoleNotAssignedToUserException.class, new ExceptionResponse(HttpStatus.BAD_REQUEST, "role.notassigned.message"));
        exceptionResponses.put(CustomDeserializationException.class, new ExceptionResponse(HttpStatus.BAD_REQUEST, "user.deserialization.error"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleException(Exception ex, Locale locale) {
        ExceptionResponse response = exceptionResponses.getOrDefault(ex.getClass(),
            new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, "general.error.message"));
        MessageResponseImpl messageResponseImpl = new MessageResponseImpl(messageSource, response.getMessageKey());
        LoggerUtil.error(messageResponseImpl.getMessage(), ex);
        return new ResponseEntity<>(messageResponseImpl, response.getStatus());
    }

    private static class ExceptionResponse {
        private HttpStatus status;
        private String messageKey;

        public ExceptionResponse(HttpStatus status, String messageKey) {
            this.status = status;
            this.messageKey = messageKey;
        }
        public HttpStatus getStatus() {
            return status;
        }
        public String getMessageKey() {
            return messageKey;
        }
    }
}
