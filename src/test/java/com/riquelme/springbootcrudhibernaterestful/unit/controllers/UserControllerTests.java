package com.riquelme.springbootcrudhibernaterestful.unit.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.riquelme.springbootcrudhibernaterestful.controllers.UserController;
import com.riquelme.springbootcrudhibernaterestful.dtos.messageResponse.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.services.UserService;

@SpringBootTest
public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserController userController;

    private User user;
    private List<User> userList;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John Doe", "john.doe@example.com");
        userList = Arrays.asList(user);
        when(userService.findAll()).thenReturn(userList);
        when(userService.findById(1L)).thenReturn(user);
        when(userService.save(any(User.class))).thenReturn(user);
        when(userService.update(eq(1L), any(User.class))).thenReturn(user);
        when(bindingResult.hasErrors()).thenReturn(false);
    }

    @Test
    public void getUsersTest() {
        ResponseEntity<MessageResponse> response = userController.getUsers();
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(userList, response.getBody().getData());
    }

    @Test
    public void getUserTest() {
        ResponseEntity<MessageResponse> response = userController.getUser(1L);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user, response.getBody().getData());
    }

    @Test
    public void createUserTest() {
        ResponseEntity<MessageResponse> response = userController.createUser(user, bindingResult);
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user, response.getBody().getData());
    }

    @Test
    public void updateUserTest() {
        ResponseEntity<MessageResponse> response = userController.updateUser(1L, user, bindingResult);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(user, response.getBody().getData());
    }

    @Test
    public void deleteUserTest() {
        ResponseEntity<?> response = userController.deleteUser(1L);
        assertEquals(NO_CONTENT, response.getStatusCode());
    }
}
