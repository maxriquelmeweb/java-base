package com.riquelme.springbootcrudhibernaterestful.integration.controller;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@PropertySource("classpath:config.properties")
public class UserControllerIntegrationTests {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UserService userService;

        @Autowired
        private MessageSource messageSource;

        @Value("${set.locale.language}")
        private String defaultLocale;

        private String getMessage(String messageKey) {
                Locale esLocale = new Locale.Builder().setLanguageTag(defaultLocale).build();
                return messageSource.getMessage(messageKey, null, esLocale);
        }

        @Nested
        class GetUsersTests {
                @Test
                void whenGetAllUsers_thenReturnsUsersList() throws Exception {
                        User user1 = new User(1L, "John Doe", "Jackson", "john@example.com", "Ea.Pas41", true);
                        user1.setCreatedAt(LocalDateTime.now().minusDays(1));
                        user1.setUpdatedAt(LocalDateTime.now());
                        user1.setRoles(new HashSet<>());

                        User user2 = new User(2L, "Jane Doe", "Smith", "jane@example.com", "Ea.Pas42", true);
                        user2.setCreatedAt(LocalDateTime.now().minusDays(1));
                        user2.setUpdatedAt(LocalDateTime.now());
                        user2.setRoles(new HashSet<>());

                        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));

                        mockMvc.perform(get("/api/users"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data", hasSize(2)))
                                        .andExpect(jsonPath("$.data[0].name", is("John Doe")))
                                        .andExpect(jsonPath("$.data[0].lastname", is("Jackson")))
                                        .andExpect(jsonPath("$.data[0].email", is("john@example.com")))
                                        .andExpect(jsonPath("$.data[0].active", is(true)))
                                        .andExpect(jsonPath("$.data[0].roles", isA(List.class)))
                                        .andExpect(jsonPath("$.data[1].name", is("Jane Doe")))
                                        .andExpect(jsonPath("$.data[1].lastname", is("Smith")))
                                        .andExpect(jsonPath("$.data[1].email", is("jane@example.com")))
                                        .andExpect(jsonPath("$.data[1].active", is(true)))
                                        .andExpect(jsonPath("$.data[1].roles", isA(List.class)))
                                        .andExpect(jsonPath("$.message", is(getMessage("user.getUsers.success"))));
                }

                @Test
                void whenGetUser_thenReturnsUser() throws Exception {
                        User user = new User(1L, "John Doe", "Jackson", "john@example.com", "Ea.Pas41", true);
                        user.setCreatedAt(LocalDateTime.now().minusDays(1));
                        user.setUpdatedAt(LocalDateTime.now());
                        user.setRoles(new HashSet<>());

                        when(userService.findById(1L)).thenReturn(user);

                        mockMvc.perform(get("/api/users/1"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data.name", is("John Doe")))
                                        .andExpect(jsonPath("$.data.lastname", is("Jackson")))
                                        .andExpect(jsonPath("$.data.email", is("john@example.com")))
                                        .andExpect(jsonPath("$.data.active", is(true)))
                                        .andExpect(jsonPath("$.data.roles", isA(List.class)))
                                        .andExpect(jsonPath("$.message", is(getMessage("user.getUser.success"))));
                }

                @Test
                void whenGetUserNotFound_thenReturns404() throws Exception {
                        when(userService.findById(anyLong()))
                                        .thenThrow(new ResourceNotFoundException("user.error.notfound"));
                        mockMvc.perform(get("/api/users/999"))
                                        .andExpect(status().isNotFound())
                                        .andExpect(jsonPath("$.message", is(getMessage("user.error.notfound"))))
                                        .andExpect(jsonPath("$.data").doesNotExist());
                }
        }

        @Nested
        class CreateUserTests {
                @Test
                void whenCreateUser_thenReturnsCreatedUser() throws Exception {
                        User user = new User(1L, "Jane Doe", "Jackson", "jane@example.com", "12345", true);
                        user.setCreatedAt(LocalDateTime.now());
                        user.setUpdatedAt(LocalDateTime.now());
                        user.setRoles(new HashSet<>());

                        when(userService.save(any(User.class))).thenReturn(user);

                        mockMvc.perform(post("/api/users")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"Jane Doe\",\"lastname\":\"Jackson\",\"email\":\"jane@example.com\",\"password\":\"12345\",\"active\":true}"))
                                        .andExpect(status().isCreated())
                                        .andExpect(jsonPath("$.data.name", is(user.getName())))
                                        .andExpect(jsonPath("$.data.lastname", is("Jackson")))
                                        .andExpect(jsonPath("$.data.email", is(user.getEmail())))
                                        .andExpect(jsonPath("$.data.active", is(true)))
                                        .andExpect(jsonPath("$.data.roles", isA(List.class)))
                                        .andExpect(jsonPath("$.message", is(getMessage("user.createUser.success"))));
                }

                @Test
                void whenCreateUserWithValidationErrors_thenReturnsBadRequest() throws Exception {
                        mockMvc.perform(post("/api/users")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"\",\"lastname\":\"\",\"email\":\"not-an-email\",\"password\":\"12345\",\"active\":true}"))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.message",
                                                        is(getMessage("handleValidationErrors.fails"))))
                                        .andExpect(jsonPath("$.data").exists());
                }

                @Test
                void whenCreateUserWithExistingEmail_thenReturnsBadRequest() throws Exception {
                        User existingUser = new User(1L, "Jane Doe", "Jackson", "jane@example.com", "12345", true);
                        existingUser.setCreatedAt(LocalDateTime.now());
                        existingUser.setUpdatedAt(LocalDateTime.now());
                        existingUser.setRoles(new HashSet<>());

                        when(userService.existsByEmail("jane@example.com")).thenReturn(true);

                        mockMvc.perform(post("/api/users")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"Jane Doe\",\"lastname\":\"Jackson\",\"email\":\"jane@example.com\",\"password\":\"12345\",\"active\":true}"))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.data.email", is(getMessage("existsByEmail.message"))));
                }
        }

        @Nested
        class UpdateUserTests {
                @Test
                void whenUpdateUser_thenReturnsUpdatedUser() throws Exception {
                        User updatedUser = new User(1L, "Jane Doe Updated", "Jackson", "janeupdated@example.com",
                                        "12345",
                                        true);
                        updatedUser.setCreatedAt(LocalDateTime.now().minusDays(1)); // Por ejemplo, fue creado ayer
                        updatedUser.setUpdatedAt(LocalDateTime.now()); // Actualizado ahora
                        updatedUser.setRoles(new HashSet<>());

                        when(userService.update(eq(1L), any(User.class))).thenReturn(updatedUser);

                        mockMvc.perform(put("/api/users/1")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"Jane Doe Updated\",\"lastname\":\"Jackson\",\"email\":\"janeupdated@example.com\",\"password\":\"12345\",\"active\":true}"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data.name", is(updatedUser.getName())))
                                        .andExpect(jsonPath("$.data.lastname", is("Jackson")))
                                        .andExpect(jsonPath("$.data.email", is(updatedUser.getEmail())))
                                        .andExpect(jsonPath("$.data.active", is(true)))
                                        .andExpect(jsonPath("$.data.roles", isA(List.class)))
                                        .andExpect(jsonPath("$.message", is(getMessage("user.updateUser.success"))));
                }
        }

        @Nested
        class DeletionUserTests {
                @Test
                void whenDeleteUser_thenReturns204() throws Exception {
                        doNothing().when(userService).deleteById(1L);
                        mockMvc.perform(delete("/api/users/1"))
                                        .andExpect(status().isNoContent())
                                        .andExpect(jsonPath("$.data").doesNotExist());
                }
        }

}