package com.riquelme.springbootcrudhibernaterestful.controllers;

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

import com.riquelme.springbootcrudhibernaterestful.dtos.RoleIdsDTO;
import com.riquelme.springbootcrudhibernaterestful.dtos.UserDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponseImpl;
import com.riquelme.springbootcrudhibernaterestful.services.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    private final UserService userService;

    public UserController(MessageSource messageSource, UserService userService) {
        super(messageSource);
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<MessageResponse> getUsers() {
        List<UserDTO> usersDTO = userService.findAll();
        return ResponseEntity.ok(new MessageResponseImpl(messageSource, "user.getUsers.success", usersDTO, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getUser(@PathVariable @Min(1) Long id) {
        UserDTO userDTO = userService.findById(id);
        return ResponseEntity.ok(new MessageResponseImpl(messageSource, "user.getUser.success", userDTO, null));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasFieldErrors()) {
            return handleValidationErrors(result);
        }
        UserDTO newUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponseImpl(messageSource, "user.createUser.success", newUser, null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable @Min(1) Long id, @Valid @RequestBody User user,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return handleValidationErrors(result);
        }
        UserDTO updateUserDTO = userService.update(id, user);
        return ResponseEntity
                .ok(new MessageResponseImpl(messageSource, "user.updateUser.success", updateUserDTO, null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Min(1) Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<MessageResponse> addRolesToUser(@PathVariable @Min(1) Long userId,
            @RequestBody RoleIdsDTO roleIdsDTO) {
        UserDTO userDTO = userService.addRolesToUser(userId, roleIdsDTO.getRoleIds());
        return ResponseEntity.ok(new MessageResponseImpl(messageSource, "user.addRoles.success", userDTO, null));
    }

    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<MessageResponse> removeRolesFromUser(@PathVariable @Min(1) Long userId,
            @RequestBody RoleIdsDTO roleIdsDTO) {
        UserDTO userDTO = userService.removeRolesFromUser(userId, roleIdsDTO.getRoleIds());
        return ResponseEntity.ok(new MessageResponseImpl(messageSource, "user.removeRoles.success", userDTO, null));
    }
}
