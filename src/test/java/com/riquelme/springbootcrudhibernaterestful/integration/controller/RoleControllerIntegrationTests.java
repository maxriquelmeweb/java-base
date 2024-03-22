package com.riquelme.springbootcrudhibernaterestful.integration.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
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

import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.services.RoleService;

@SpringBootTest
@AutoConfigureMockMvc
@PropertySource("classpath:config.properties")
public class RoleControllerIntegrationTests {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RoleService roleService;

        @Autowired
        private MessageSource messageSource;

        @Value("${set.locale.language}")
        private String defaultLocale;

        private String getMessage(String messageKey) {
                Locale esLocale = new Locale.Builder().setLanguageTag(defaultLocale).build();
                String resultMessage = messageSource.getMessage(messageKey, null, esLocale);
                return resultMessage;
        }

        // Usando @Nested para organizar los tests en clases anónimas
        @Nested
        class GetRolesTests {
                @Test
                void whenGetAllRoles_thenReturnsRolesList() throws Exception {
                        Role adminRole = new Role(1L, "Admin");
                        adminRole.setCreated_at(
                                        LocalDateTime.parse("2024-03-20T20:31:36.646439",
                                                        DateTimeFormatter.ISO_DATE_TIME));
                        adminRole.setUpdated_at(
                                        LocalDateTime.parse("2024-03-20T20:31:36.646439",
                                                        DateTimeFormatter.ISO_DATE_TIME));
                        adminRole.setUsers(new HashSet<>());

                        Role userRole = new Role(2L, "User");
                        userRole.setCreated_at(
                                        LocalDateTime.parse("2024-03-20T20:33:04.279875",
                                                        DateTimeFormatter.ISO_DATE_TIME));
                        userRole.setUpdated_at(
                                        LocalDateTime.parse("2024-03-20T20:33:04.279875",
                                                        DateTimeFormatter.ISO_DATE_TIME));
                        userRole.setUsers(new HashSet<>());

                        when(roleService.findAll()).thenReturn(Arrays.asList(adminRole, userRole));

                        mockMvc.perform(get("/api/roles"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data", hasSize(2)))
                                        .andExpect(jsonPath("$.data[0].name", is("Admin")))
                                        .andExpect(jsonPath("$.data[1].name", is("User")))
                                        .andExpect(jsonPath("$.message", is(getMessage("role.getRoles.success"))));
                }

                @Test
                void whenGetRole_thenReturnsRole() throws Exception {
                        Role role = new Role(1L, "Admin");
                        role.setCreated_at(
                                        LocalDateTime.parse("2024-03-20T20:31:36.646439",
                                                        DateTimeFormatter.ISO_DATE_TIME));
                        role.setUpdated_at(
                                        LocalDateTime.parse("2024-03-20T20:31:36.646439",
                                                        DateTimeFormatter.ISO_DATE_TIME));
                        role.setUsers(new HashSet<>());

                        when(roleService.findById(1L)).thenReturn(role);
                        mockMvc.perform(get("/api/roles/1"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data.name", is(role.getName())))
                                        .andExpect(jsonPath("$.message", is(getMessage("role.getRole.success"))));
                }

                @Test
                void whenGetRoleNotFound_thenReturns404() throws Exception {
                        when(roleService.findById(anyLong()))
                                        .thenThrow(new ResourceNotFoundException("role.error.notfound"));
                        mockMvc.perform(get("/api/roles/999"))
                                        .andExpect(status().isNotFound())
                                        .andExpect(jsonPath("$.message", is(getMessage("role.error.notfound"))))
                                        .andExpect(jsonPath("$.data").doesNotExist());
                }
        }

        @Nested
        class CreateRoleTests {
                @Test
                void whenCreateRole_thenReturnsCreatedRole() throws Exception {
                        Role role = new Role(1L, "Admin");
                        role.setCreated_at(LocalDateTime.parse("2024-03-20T20:31:36.646439",
                                        DateTimeFormatter.ISO_DATE_TIME));
                        role.setUpdated_at(LocalDateTime.parse("2024-03-20T20:31:36.646439",
                                        DateTimeFormatter.ISO_DATE_TIME));
                        role.setUsers(new HashSet<>());

                        when(roleService.save(any(Role.class))).thenReturn(role);

                        mockMvc.perform(post("/api/roles")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"Admin\"}"))
                                        .andExpect(status().isCreated())
                                        .andExpect(jsonPath("$.data.name", is(role.getName())))
                                        .andExpect(jsonPath("$.message", is(getMessage("role.createRole.success"))));
                }

                @Test
                void whenCreateRoleWithValidationErrors_thenReturnsBadRequest() throws Exception {
                        mockMvc.perform(post("/api/roles")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"\"}"))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.message",
                                                        is(getMessage("handleValidationErrors.fails"))))
                                        .andExpect(jsonPath("$.data").exists());
                }

                @Test
                void whenCreateRoleWithExistingName_thenReturnsBadRequest() throws Exception {
                        String roleName = "Admin";
                        when(roleService.existsByName(roleName)).thenReturn(true);
                        mockMvc.perform(post("/api/roles")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"" + roleName + "\"}"))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.data.name",
                                                        is(getMessage("existsByNameRole.message"))));
                }
        }

        @Nested
        class UpdateRoleTests {
                @Test
                void whenUpdateRole_thenReturnsUpdatedRole() throws Exception {
                        Role updatedRole = new Role(1L, "AdminUpdated");
                        updatedRole.setCreated_at(
                                        LocalDateTime.parse("2024-03-20T20:31:36.646439",
                                                        DateTimeFormatter.ISO_DATE_TIME));
                        updatedRole.setUpdated_at(LocalDateTime.now());
                        updatedRole.setUsers(new HashSet<>());

                        when(roleService.update(eq(1L), any(Role.class))).thenReturn(updatedRole);

                        mockMvc.perform(put("/api/roles/1")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"AdminUpdated\"}"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data.name", is(updatedRole.getName())))
                                        .andExpect(jsonPath("$.message", is(getMessage("role.updateRole.success"))));
                }
        }

        @Nested
        class DeleteRoleTests {
                @Test
                void whenDeleteRole_thenReturns204() throws Exception {
                        doNothing().when(roleService).deleteById(1L);
                        mockMvc.perform(delete("/api/roles/1"))
                                        .andExpect(status().isNoContent())
                                        .andExpect(jsonPath("$.data").doesNotExist());
                }
        }

}
