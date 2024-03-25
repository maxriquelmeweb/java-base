package com.riquelme.javabase.integration.controllers;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.isA;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riquelme.javabase.dtos.RoleIdsDTO;
import com.riquelme.javabase.dtos.UserDTO;
import com.riquelme.javabase.entities.User;
import com.riquelme.javabase.exceptions.NotFoundException;
import com.riquelme.javabase.services.UserService;

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
                        UserDTO user1DTO = new UserDTO(1L, "John Doe", "Jackson", "john@example.com", true,
                                        new HashSet<>());
                        UserDTO user2DTO = new UserDTO(2L, "Jane Doe", "Smith", "jane@example.com", true,
                                        new HashSet<>());

                        when(userService.findAll()).thenReturn(Arrays.asList(user1DTO, user2DTO));

                        mockMvc.perform(get("/api/users")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON))
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
                        UserDTO userDTO = new UserDTO(1L, "John Doe", "Jackson", "john@example.com", true,
                                        new HashSet<>());

                        when(userService.findById(1L)).thenReturn(userDTO);

                        mockMvc.perform(get("/api/users/1")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON))
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
                                        .thenThrow(new NotFoundException("user.notfound.message"));
                        mockMvc.perform(get("/api/users/999")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isNotFound())
                                        .andExpect(jsonPath("$.message", is(getMessage("user.notfound.message"))))
                                        .andExpect(jsonPath("$.data").doesNotExist());
                }
        }

        @Nested
        class CreateUserTests {
                @Test
                void whenCreateUser_thenReturnsCreatedUser() throws Exception {
                        UserDTO userDTO = new UserDTO(1L, "John", "Jackson", "john@example.com", true,
                                        new HashSet<>());

                        when(userService.save(any(User.class))).thenReturn(userDTO);

                        mockMvc.perform(post("/api/users")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"Jane\",\"lastname\":\"Jackson\",\"email\":\"jane@example.com\",\"password\":\"12345\",\"active\":true}"))
                                        .andExpect(status().isCreated())
                                        .andExpect(jsonPath("$.data.name", is(userDTO.getName())))
                                        .andExpect(jsonPath("$.data.lastname", is("Jackson")))
                                        .andExpect(jsonPath("$.data.email", is(userDTO.getEmail())))
                                        .andExpect(jsonPath("$.data.active", is(true)))
                                        .andExpect(jsonPath("$.data.roles", isA(List.class)))
                                        .andExpect(jsonPath("$.message", is(getMessage("user.createUser.success"))));
                }

                @Test
                void whenCreateUserWithValidationErrors_thenReturnsBadRequest() throws Exception {
                        mockMvc.perform(post("/api/users")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"\",\"lastname\":\"\",\"email\":\"not-an-email\",\"password\":\"12345\",\"active\":true}"))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.message",
                                                        is(getMessage("error.validation"))))
                                        .andExpect(jsonPath("$.data").exists());
                }

                @Test
                void whenCreateUserWithExistingEmail_thenReturnsBadRequest() throws Exception {
                        User existingUser = new User(1L, "Jane", "Jackson", "jane@example.com", "12345", true);
                        existingUser.setCreatedAt(LocalDateTime.now());
                        existingUser.setUpdatedAt(LocalDateTime.now());
                        existingUser.setRoles(new HashSet<>());

                        when(userService.existsByEmail("jane@example.com")).thenReturn(true);

                        mockMvc.perform(post("/api/users")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"Jane\",\"lastname\":\"Jackson\",\"email\":\"jane@example.com\",\"password\":\"12345\",\"active\":true}"))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.data.email",
                                                        is(getMessage("user.email.exists.message"))));
                }
        }

        @Nested
        class UpdateUserTests {
                @Test
                void whenUpdateUser_thenReturnsUpdatedUser() throws Exception {
                        UserDTO userDTO = new UserDTO(1L, "John", "Jackson", "john@example.com", true,
                                        new HashSet<>());

                        when(userService.update(eq(1L), any(User.class))).thenReturn(userDTO);

                        mockMvc.perform(put("/api/users/1")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"Jane\",\"lastname\":\"Jackson\",\"email\":\"janeupdated@example.com\",\"password\":\"12345\",\"active\":true}"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data.name", is(userDTO.getName())))
                                        .andExpect(jsonPath("$.data.lastname", is("Jackson")))
                                        .andExpect(jsonPath("$.data.email", is(userDTO.getEmail())))
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
                        mockMvc.perform(delete("/api/users/1")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isNoContent())
                                        .andExpect(jsonPath("$.data").doesNotExist());
                }
        }

        @Nested
        class RoleManagementTests {

                @Test
                void whenAddRolesToUser_thenReturnsUpdatedUser() throws Exception {
                        Long userId = 1L;
                        RoleIdsDTO roleIdsDTO = new RoleIdsDTO();
                        roleIdsDTO.setRoleIds(new HashSet<>(Arrays.asList(1L, 2L)));

                        UserDTO userWithRoles = new UserDTO();
                        userWithRoles.setId(userId);

                        when(userService.addRolesToUser(eq(userId), anySet())).thenReturn(userWithRoles);

                        mockMvc.perform(post("/api/users/{userId}/roles", userId)
                                        .with(user("admin").roles("ADMIN"))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(roleIdsDTO)))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data").isNotEmpty())
                                        .andExpect(jsonPath("$.message", is(getMessage("user.addRoles.success"))));
                }

                @Test
                void whenRemoveRolesFromUser_thenReturnsUpdatedUser() throws Exception {
                        Long userId = 1L;
                        RoleIdsDTO roleIdsDTO = new RoleIdsDTO();
                        roleIdsDTO.setRoleIds(new HashSet<>(Arrays.asList(1L, 2L)));

                        UserDTO userWithoutRoles = new UserDTO();
                        userWithoutRoles.setId(userId);

                        when(userService.removeRolesFromUser(eq(userId), anySet())).thenReturn(userWithoutRoles);

                        mockMvc.perform(delete("/api/users/{userId}/roles", userId)
                                        .with(user("admin").roles("ADMIN"))
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(roleIdsDTO)))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data").isNotEmpty())
                                        .andExpect(jsonPath("$.message", is(getMessage("user.removeRoles.success"))));
                }
        }

}