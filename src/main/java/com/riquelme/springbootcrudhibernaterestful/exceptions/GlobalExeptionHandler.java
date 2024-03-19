package com.riquelme.springbootcrudhibernaterestful.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponseImpl;

@RestControllerAdvice
public class GlobalExeptionHandler {

    private final MessageSource messageSource;

    public GlobalExeptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleAllExceptions(Exception ex, WebRequest request) {
        // ex para loggin
        return new ResponseEntity<>(new MessageResponseImpl(messageSource, "general.error.message", null, null),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<MessageResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
            WebRequest request) {
        return new ResponseEntity<>(new MessageResponseImpl(messageSource, ex.getResourceType(), null, null),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<MessageResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String requiredTypeName = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown";
        Object[] args = { e.getValue(), e.getName(), requiredTypeName };
        return new ResponseEntity<>(new MessageResponseImpl(messageSource, "type.mismatch.error", null, args),
                HttpStatus.BAD_REQUEST);
    }

}
