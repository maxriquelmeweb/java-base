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
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<MessageResponse> getUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(new MessageSuccessDTO("Lista de usuarios obtenida exitosamente.", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(new MessageSuccessDTO("Usuario obtenido exitosamente.", user));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        User newUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageSuccessDTO("Usuario creado exitosamente.", newUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id, @Valid @RequestBody User user,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return validation(result);
        }
        User updateUser = userService.update(id, user);
        return ResponseEntity.ok(new MessageSuccessDTO("Usuario actualizado exitosamente.", updateUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<MessageResponse> validation(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(
                error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new MessageErrorDTO("hay errores al validar campos.", errors));
    }
}
