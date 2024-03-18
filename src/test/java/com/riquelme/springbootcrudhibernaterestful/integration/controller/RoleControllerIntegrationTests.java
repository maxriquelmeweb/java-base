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

import com.riquelme.springbootcrudhibernaterestful.controllers.RoleController;
import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.errors.RoleNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.services.RoleService;

@WebMvcTest(RoleController.class)
public class RoleControllerIntegrationTests {

        @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Test
    public void whenGetAllRoles_thenReturnsRolesList() throws Exception {
        when(roleService.findAll()).thenReturn(Arrays.asList(
                new Role(1L, "Admin"),
                new Role(2L, "User")));

        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].name", is("Admin")))
                .andExpect(jsonPath("$.data[1].name", is("User")));
    }

    @Test
    public void whenGetRole_thenReturnsRole() throws Exception {
        Role role = new Role(1L, "Admin");
        when(roleService.findById(1L)).thenReturn(role);

        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(role.getName())));
    }

    @Test
    public void whenGetRoleNotFound_thenReturns404() throws Exception {
        when(roleService.findById(anyLong())).thenThrow(new RoleNotFoundException("Rol no encontrado."));

        mockMvc.perform(get("/api/roles/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Rol no encontrado.")));
    }

    @Test
    public void whenCreateRole_thenReturnsCreatedRole() throws Exception {
        Role role = new Role(1L, "Admin");
        when(roleService.save(any(Role.class))).thenReturn(role);

        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Admin\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name", is(role.getName())));
    }

    @Test
    public void whenDeleteRole_thenReturns204() throws Exception {
        doNothing().when(roleService).deleteById(1L);

        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenUpdateRole_thenReturnsUpdatedRole() throws Exception {
        Role updatedRole = new Role(1L, "Admin");
        when(roleService.update(eq(1L), any(Role.class))).thenReturn(updatedRole);

        mockMvc.perform(put("/api/roles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Admin\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name", is(updatedRole.getName())));
    }

    @Test
    public void whenCreateRoleWithValidationErrors_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("hay errores al validar campos.")))
                .andExpect(jsonPath("$.data.name", is("size must be between 2 and 50")));
    }

}
