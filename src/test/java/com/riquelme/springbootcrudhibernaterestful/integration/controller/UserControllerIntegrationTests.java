package com.riquelme.springbootcrudhibernaterestful.integration.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.riquelme.springbootcrudhibernaterestful.controllers.UserController;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.services.UserService;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void whenGetAllUsers_thenReturnsUsersList() throws Exception {
        when(userService.findAll()).thenReturn(Arrays.asList(
                new User(1L, "John Doe", "Jackson", "john@example.com", "Ea.Pas41"),
                new User(2L, "Jane Doe", "Smith", "jane@example.com", "Ea.Pas42")));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("John Doe")))
                .andExpect(jsonPath("$.data[1].name", is("Jane Doe")));
    }

    @Test
    public void whenGetUser_thenReturnsUser() throws Exception {
        User user = new User(1L, "John Doe", "Jackson", "john@example.com", "Ea.Pas41");
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(user.getName())))
                .andExpect(jsonPath("$.data.email", is(user.getEmail())));
    }

    @Test
    public void whenGetUserNotFound_thenReturns404() throws Exception {
        when(userService.findById(anyLong())).thenThrow(new ResourceNotFoundException("Usuario no encontrado."));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Usuario no encontrado.")));
    }

    @Test
    public void whenCreateUser_thenReturnsCreatedUser() throws Exception {
        User user = new User(1L, "Jane Doe", "Jackson", "jane@example.com", "12345");
        when(userService.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jane Doe\",\"lastname\":\"Jackson\",\"email\":\"jane@example.com\",\"password\":\"12345\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name", is(user.getName())))
                .andExpect(jsonPath("$.data.email", is(user.getEmail())));
    }

    @Test
    public void whenDeleteUser_thenReturns204() throws Exception {
        doNothing().when(userService).deleteById(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenUpdateUser_thenReturnsUpdatedUser() throws Exception {
        User updatedUser = new User(1L, "Jane Doe Updated", "Jackson", "janeupdated@example.com", "12345");
        when(userService.update(eq(1L), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jane Doe Updated\",\"lastname\":\"Jackson\",\"email\":\"janeupdated@example.com\",\"password\":\"12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(updatedUser.getName())))
                .andExpect(jsonPath("$.data.email", is(updatedUser.getEmail())));
    }

    @Test
    public void whenCreateUserWithValidationErrors_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\",\"lastname\":\"\",\"email\":\"not-an-email\",\"password\":\"12\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("hay errores al validar campos.")))
                .andExpect(jsonPath("$.data.name", is("must not be blank")))
                .andExpect(jsonPath("$.data.email", is("must be a well-formed email address")));
    }
}