package com.riquelme.javabase.validations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.riquelme.javabase.services.RoleService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
@RequestScope
public class ExistsByNameRoleValidation implements ConstraintValidator<ExistsByNameRole, String> {
    @Autowired
    private RoleService service;

    @Autowired
    private HttpServletRequest request;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (service == null) {
            return true;
        }

        // Verificar el método de la solicitud HTTP
        String httpMethod = request.getMethod();

        // Si es una solicitud PUT, asumimos que es una actualización y no aplicamos la validación
        if ("PUT".equalsIgnoreCase(httpMethod)) {
            return true;
        }

        // Aplicar la validación solo para métodos distintos de PUT (como POST para creación)
        return !service.existsByName(email);
    }
}
