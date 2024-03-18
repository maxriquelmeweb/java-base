package com.riquelme.springbootcrudhibernaterestful.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.riquelme.springbootcrudhibernaterestful.dtos.messageResponse.MessageErrorDTO;
import com.riquelme.springbootcrudhibernaterestful.dtos.messageResponse.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.dtos.messageResponse.MessageSuccessDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.services.RoleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<MessageResponse> getRoles() {
        List<Role> roles = roleService.findAll();
        return ResponseEntity.ok(new MessageSuccessDTO("Lista de roles obtenida exitosamente.", roles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getRole(@PathVariable Long id) {
        Role role = roleService.findById(id);
        return ResponseEntity.ok(new MessageSuccessDTO("Rol obtenido exitosamente.", role));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createRole(@Valid @RequestBody Role role, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Role newRole = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageSuccessDTO("Rol creado exitosamente.", newRole));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRole(@PathVariable Long id, @Valid @RequestBody Role role,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        Role updateRole = roleService.update(id, role);
        return ResponseEntity.ok(new MessageSuccessDTO("Rol actualizado exitosamente.", updateRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<MessageResponse> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new MessageErrorDTO("hay errores al validar campos.", errors));
    }
}
