package com.riquelme.javabase.controllers;

import java.util.List;

import org.springframework.context.MessageSource;
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

import com.riquelme.javabase.dtos.RoleDTO;
import com.riquelme.javabase.entities.Role;
import com.riquelme.javabase.responses.MessageResponse;
import com.riquelme.javabase.responses.MessageResponseImpl;
import com.riquelme.javabase.services.RoleService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends BaseController {

    private final RoleService roleService;

    public RoleController(MessageSource messageSource, RoleService roleService) {
        super(messageSource);
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<MessageResponse> getRoles() {
        List<RoleDTO> rolesDTO = roleService.findAll();
        return ResponseEntity.ok(new MessageResponseImpl(messageSource, "role.getRoles.success", rolesDTO));
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getRole(@PathVariable @Min(1) Long id) {
        RoleDTO roleDTO = roleService.findById(id);
        return ResponseEntity
                .ok(new MessageResponseImpl(messageSource, "role.getRole.success", roleDTO));
    }

    @Transactional
    @PostMapping
    public ResponseEntity<MessageResponse> createRole(@Valid @RequestBody Role role, BindingResult result) {
        if (result.hasErrors()) {
            return handleValidationErrors(result);
        }
        RoleDTO newRoleDTO = roleService.save(role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponseImpl(messageSource, "role.createRole.success", newRoleDTO));
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateRole(@PathVariable @Min(1) Long id,
            @Valid @RequestBody Role role,
            BindingResult result) {
        if (result.hasErrors()) {
            return handleValidationErrors(result);
        }
        RoleDTO updatedRole = roleService.update(id, role);
        return ResponseEntity
                .ok(new MessageResponseImpl(messageSource, "role.updateRole.success", updatedRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable @Min(1) Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
