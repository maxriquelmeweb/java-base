package com.riquelme.springbootcrudhibernaterestful.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.UserRepository;
import com.riquelme.springbootcrudhibernaterestful.services.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "Doe", "john.doe@example.com", "password", true);
    }

    @Test
    void whenFindAll_thenReturnUserList() {
        User user2 = new User(2L, "Jane", "Doe", "jane.doe@example.com", "password", true);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertTrue(users.contains(user) && users.contains(user2));
    }

    @Test
    void whenFindById_thenReturnUser() {
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
    void whenSave_thenPersistUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        assertNotNull(savedUser);
        assertEquals("John", savedUser.getName());
    }

    @Test
    void whenUpdate_thenUpdateUserDetails() {
        User updatedUser = new User(1L, "John", "Doe", "john.update@example.com", "newpassword", true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = userService.update(1L, updatedUser);

        assertNotNull(result);
        assertEquals("john.update@example.com", result.getEmail());
    }

    @Test
    void whenDeleteById_thenUserShouldBeDeleted() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        userService.deleteById(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void whenExistsByEmail_thenReturnTrueOrFalse() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);

        boolean exists = userService.existsByEmail("john.doe@example.com");

        assertTrue(exists);
    }
}