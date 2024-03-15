package com.riquelme.springbootcrudhibernaterestful.integration.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.riquelme.springbootcrudhibernaterestful.controllers.UserController;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.errors.UserNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.services.UserService;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void whenGetUser_thenReturnsUser() throws Exception {
        User user = new User(1L, "John Doe", "john@example.com");
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(user.getName())))
                .andExpect(jsonPath("$.data.email", is(user.getEmail())));
    }

    @Test
    public void whenGetUserNotFound_thenReturns404() throws Exception {
        when(userService.findById(anyLong())).thenThrow(new UserNotFoundException("Usuario no encontrado."));

        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Usuario no encontrado.")));
    }

    @Test
    public void whenCreateUser_thenReturnsCreatedUser() throws Exception {
        User user = new User(1L, "Jane Doe", "jane@example.com");
        when(userService.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Jane Doe\",\"email\":\"jane@example.com\"}"))
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

    // agregar más pruebas para PUT y manejo de errores específicos.
}