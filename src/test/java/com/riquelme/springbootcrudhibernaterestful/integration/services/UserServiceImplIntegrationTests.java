package com.riquelme.springbootcrudhibernaterestful.integration.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.RoleRepository;
import com.riquelme.springbootcrudhibernaterestful.repositories.UserRepository;
import com.riquelme.springbootcrudhibernaterestful.services.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplIntegrationTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void whenFindAll_thenReturnUserList() {
        User user1 = new User(1L, "John", "Doe", "john@example.com", "password123", true);
        User user2 = new User(2L, "Jane", "Doe", "jane@example.com", "password123", true);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnUser() {
        User user = new User(1L, "John", "Doe", "john@example.com", "password123", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals("John", foundUser.getName());
    }

    @Test
    void whenFindById_thenThrowResourceNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    @Transactional
    void whenSave_thenPersistUser() {
        // Crear el rol "USER" y simular su recuperación
        Role userRole = new Role(1L, "USER");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(userRole));

        // Crear un nuevo usuario
        User newUser = new User(null, "New User", "Lastname", "newuser@example.com", "password123", true);

        // Simular la acción de guardar, que incluye la asignación del rol "USER"
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.getRoles().add(userRole);
            return user;
        });

        // Invocar el método de guardar del servicio
        User savedUser = userService.save(newUser);

        // Verificar que el usuario se guardó correctamente y que se le asignó el rol
        // "USER"
        assertNotNull(savedUser);
        assertEquals("New User", savedUser.getName());
        assertTrue(savedUser.getRoles().contains(userRole), "The user should have the USER role assigned");
    }

    @Test
    @Transactional
    void whenUpdate_thenUpdateUserDetails() {
        User existingUser = new User(1L, "John", "Doe", "john@example.com", "password123", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User updatedUser = new User(1L, "Updated Name", "Updated Lastname", "updated@example.com", "password123", true);
        User resultUser = userService.update(1L, updatedUser);

        assertNotNull(resultUser);
        assertEquals("Updated Name", resultUser.getName());
    }

    @Test
    @Transactional
    void whenDeleteById_thenUserShouldBeDeleted() {
        User user = new User(1L, "John", "Doe", "john@example.com", "password123", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(1L);

        verify(userRepository).delete(user);
    }

    @Test
    void whenExistsByEmail_thenReturnTrueOrFalse() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
        boolean exists = userService.existsByEmail("john@example.com");

        assertTrue(exists);
    }
}