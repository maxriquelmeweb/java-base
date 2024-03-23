package com.riquelme.springbootcrudhibernaterestful.controllers;

import java.util.List;
import java.util.stream.Collectors;

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
import com.riquelme.springbootcrudhibernaterestful.util.EntityDtoMapper;

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
        List<User> users = userService.findAll();
        List<UserDTO> userDTOs = users.stream()
                .map(user -> EntityDtoMapper.convertToDTO(user, UserDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new MessageResponseImpl(messageSource, "user.getUsers.success", userDTOs, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageResponse> getUser(@PathVariable @Min(1) Long id) {
        User user = userService.findById(id);
        return ResponseEntity
                .ok(new MessageResponseImpl(messageSource, "user.getUser.success",
                        EntityDtoMapper.convertToDTO(user, UserDTO.class), null));
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasFieldErrors()) {
            return handleValidationErrors(result);
        }
        User newUser = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponseImpl(messageSource, "user.createUser.success",
                        EntityDtoMapper.convertToDTO(newUser, UserDTO.class),
                        null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable @Min(1) Long id, @Valid @RequestBody User user,
            BindingResult result) {
        if (result.hasFieldErrors()) {
            return handleValidationErrors(result);
        }
        User updateUser = userService.update(id, user);
        return ResponseEntity
                .ok(new MessageResponseImpl(messageSource, "user.updateUser.success",
                        EntityDtoMapper.convertToDTO(updateUser, UserDTO.class),
                        null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @Min(1) Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<MessageResponse> addRolesToUser(@PathVariable @Min(1) Long userId,
            @RequestBody RoleIdsDTO roleIdsDTO) {
        User user = userService.addRolesToUser(userId, roleIdsDTO.getRoleIds());
        return ResponseEntity.ok(new MessageResponseImpl(messageSource, "user.addRoles.success",
                EntityDtoMapper.convertToDTO(user, UserDTO.class), null));
    }

    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<MessageResponse> removeRolesFromUser(@PathVariable @Min(1) Long userId,
            @RequestBody RoleIdsDTO roleIdsDTO) {
        User user = userService.removeRolesFromUser(userId, roleIdsDTO.getRoleIds());
        return ResponseEntity.ok(new MessageResponseImpl(messageSource, "user.removeRoles.success",
                EntityDtoMapper.convertToDTO(user, UserDTO.class), null));
    }
}
