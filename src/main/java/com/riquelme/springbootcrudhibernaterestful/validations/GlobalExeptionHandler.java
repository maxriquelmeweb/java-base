package com.riquelme.springbootcrudhibernaterestful.validations;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.riquelme.springbootcrudhibernaterestful.errors.UserNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.responses.ErrorResponse;

@RestControllerAdvice
public class GlobalExeptionHandler {

    private final Environment env;

    public GlobalExeptionHandler(Environment env) {
        this.env = env;
    }

    // errores generales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                env.getProperty("general.error.message", "Ocurrio un error inesperado."));
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // errores especificos con clase especifica
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                env.getProperty("user.error.notfound", "Usuario no encontrado."));
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // errores especificos con clase ya creada
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String requiredTypeName = e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "desconocido";
        String message = String.format(
                "El valor '%s' no es válido para el campo '%s'. Se requiere un campo de tipo '%s'.",
                e.getValue(), e.getName(), requiredTypeName);
        ErrorResponse errorResponse = new ErrorResponse(
                env.getProperty("general.error.message", message));
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
