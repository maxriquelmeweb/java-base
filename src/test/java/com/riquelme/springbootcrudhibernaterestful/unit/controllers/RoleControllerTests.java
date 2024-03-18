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

import com.riquelme.springbootcrudhibernaterestful.controllers.RoleController;
import com.riquelme.springbootcrudhibernaterestful.dtos.messageResponse.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.services.RoleService;

@SpringBootTest
public class RoleControllerTests {

    @Mock
    private RoleService roleService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private RoleController roleController;

    private Role role;
    private List<Role> roleList;

    @BeforeEach
    void setUp() {
        role = new Role(1L, "Admin");
        roleList = Arrays.asList(role);
        when(roleService.findAll()).thenReturn(roleList);
        when(roleService.findById(1L)).thenReturn(role);
        when(roleService.save(any(Role.class))).thenReturn(role);
        when(roleService.update(eq(1L), any(Role.class))).thenReturn(role);
        when(bindingResult.hasErrors()).thenReturn(false);
    }

    @Test
    public void getRolesTest() {
        ResponseEntity<MessageResponse> response = roleController.getRoles();
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(roleList, response.getBody().getData());
    }

    @Test
    public void getRoleTest() {
        ResponseEntity<MessageResponse> response = roleController.getRole(1L);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(role, response.getBody().getData());
    }

    @Test
    public void createRoleTest() {
        ResponseEntity<MessageResponse> response = roleController.createRole(role, bindingResult);
        assertEquals(CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(role, response.getBody().getData());
    }

    @Test
    public void updateRoleTest() {
        ResponseEntity<MessageResponse> response = roleController.updateRole(1L, role, bindingResult);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(role, response.getBody().getData());
    }

    @Test
    public void deleteRoleTest() {
        ResponseEntity<?> response = roleController.deleteRole(1L);
        assertEquals(NO_CONTENT, response.getStatusCode());
    }
}
