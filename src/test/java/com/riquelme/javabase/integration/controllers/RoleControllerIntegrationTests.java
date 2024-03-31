package com.riquelme.javabase.integration.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Locale;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.riquelme.javabase.dtos.RoleDTO;
import com.riquelme.javabase.entities.Role;
import com.riquelme.javabase.exceptions.NotFoundException;
import com.riquelme.javabase.services.RoleService;
import com.riquelme.javabase.util.EntityDtoMapper;

@SpringBootTest
@AutoConfigureMockMvc
@PropertySource("classpath:config.properties")
public class RoleControllerIntegrationTests {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private RoleService roleService;

        @Mock
        private EntityDtoMapper entityDtoMapper;

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
                        RoleDTO adminRoleDTO = new RoleDTO(1L, "Admin");
                        RoleDTO userRoleDTO = new RoleDTO(2L, "User");

                        when(roleService.findAll()).thenReturn(Arrays.asList(adminRoleDTO, userRoleDTO));

                        mockMvc.perform(get("/api/roles")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data", hasSize(2)))
                                        .andExpect(jsonPath("$.data[0].name", is("Admin")))
                                        .andExpect(jsonPath("$.data[1].name", is("User")))
                                        .andExpect(jsonPath("$.message", is(getMessage("role.getRoles.success"))));
                }

                @Test
                void whenGetRole_thenReturnsRole() throws Exception {
                        RoleDTO roleDTO = new RoleDTO(1L, "Admin");
                        when(roleService.findById(1L)).thenReturn(roleDTO);
                        mockMvc.perform(get("/api/roles/1")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data.name", is(roleDTO.getName())))
                                        .andExpect(jsonPath("$.message", is(getMessage("role.getRole.success"))));
                }

                @Test
                void whenGetRoleNotFound_thenReturns404() throws Exception {
                        when(roleService.findById(anyLong()))
                                        .thenThrow(new NotFoundException("role.notFound.message"));
                        mockMvc.perform(get("/api/roles/999")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isNotFound())
                                        .andExpect(jsonPath("$.message", is(getMessage("role.notFound.message"))))
                                        .andExpect(jsonPath("$.data").doesNotExist());
                }
        }

        @Nested
        class CreateRoleTests {
                @Test
                void whenCreateRole_thenReturnsCreatedRole() throws Exception {
                        RoleDTO roleDTO = new RoleDTO(1L, "Admin");
                        when(roleService.save(any(Role.class))).thenReturn(roleDTO);
                        mockMvc.perform(post("/api/roles")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"Admin\"}"))
                                        .andExpect(status().isCreated())
                                        .andExpect(jsonPath("$.data.name", is(roleDTO.getName())))
                                        .andExpect(jsonPath("$.message", is(getMessage("role.createRole.success"))));
                }

                @Test
                void whenCreateRoleWithValidationErrors_thenReturnsBadRequest() throws Exception {
                        mockMvc.perform(post("/api/roles")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"\"}"))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.message",
                                                        is(getMessage("error.validation"))))
                                        .andExpect(jsonPath("$.data").exists());
                }

                @Test
                void whenCreateRoleWithExistingName_thenReturnsBadRequest() throws Exception {
                        String roleName = "Admin";
                        when(roleService.existsByName(roleName)).thenReturn(true);
                        mockMvc.perform(post("/api/roles")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"" + roleName + "\"}"))
                                        .andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.data.name",
                                                        is(getMessage("role.existsByNameRole.message"))));
                }
        }

        @Nested
        class UpdateRoleTests {
                @Test
                void whenUpdateRole_thenReturnsUpdatedRole() throws Exception {
                        RoleDTO updatedRoleDTO = new RoleDTO(1L, "AdminUpdated");

                        when(roleService.update(eq(1L), any(Role.class))).thenReturn(updatedRoleDTO);

                        mockMvc.perform(put("/api/roles/1")
                                        .with(user("admin").roles("ADMIN")) // Simula un usuario autenticado con rol
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content("{\"name\":\"AdminUpdated\"}"))
                                        .andExpect(status().isOk())
                                        .andExpect(jsonPath("$.data.name", is(updatedRoleDTO.getName())))
                                        .andExpect(jsonPath("$.message", is(getMessage("role.updateRole.success"))));
                }
        }

        @Nested
        class DeleteRoleTests {
                @Test
                void whenDeleteRole_thenReturns204() throws Exception {
                        doNothing().when(roleService).deleteById(1L);
                        mockMvc.perform(delete("/api/roles/1")
                                        .with(user("admin").roles("ADMIN")) // Aquí se simula el usuario autenticado
                                        .contentType(MediaType.APPLICATION_JSON))
                                        .andExpect(status().isNoContent())
                                        .andExpect(jsonPath("$.data").doesNotExist());
                }
        }

}
