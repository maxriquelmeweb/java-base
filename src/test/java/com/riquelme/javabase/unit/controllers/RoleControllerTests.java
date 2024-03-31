package com.riquelme.javabase.unit.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.riquelme.javabase.controllers.RoleController;
import com.riquelme.javabase.dtos.RoleDTO;
import com.riquelme.javabase.entities.Role;
import com.riquelme.javabase.responses.MessageResponse;
import com.riquelme.javabase.services.RoleService;

@SpringBootTest
public class RoleControllerTests {

    @Mock
    private RoleService roleService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private RoleController roleController;

    private Role role;
    private RoleDTO roleDTO;
    private List<RoleDTO> roleDTOList;

    @BeforeEach
    void setUp() {
        role = new Role(1L, "Admin");
        roleDTO = new RoleDTO(1L, "Admin");
        roleDTOList = Arrays.asList(roleDTO);

        when(roleService.findAll()).thenReturn(roleDTOList);
        when(roleService.findById(1L)).thenReturn(roleDTO);
        when(roleService.save(any(Role.class))).thenReturn(roleDTO);
        when(roleService.update(eq(1L), any(Role.class))).thenReturn(roleDTO);
        when(bindingResult.hasErrors()).thenReturn(false);
    }

    @Nested
    @DisplayName("Get Roles Tests")
    class GetRolesTests {
        @Test
        @DisplayName("When getting all roles, return status OK with correct data")
        public void getRolesTest() {
            ResponseEntity<MessageResponse> response = roleController.getRoles();
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            List<?> responseData = (List<?>) response.getBody().getData();
            assertEquals(roleDTOList.size(), responseData.size());
            assertEquals(roleDTOList.get(0).getName(), ((RoleDTO) responseData.get(0)).getName());
        }

        @Test
        @DisplayName("When getting a specific role by ID, return status OK with correct data")
        public void getRoleTest() {
            ResponseEntity<MessageResponse> response = roleController.getRole(1L);
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            RoleDTO responseData = (RoleDTO) response.getBody().getData();
            assertEquals(roleDTO.getId(), responseData.getId());
            assertEquals(roleDTO.getName(), responseData.getName());
        }
    }

    @Nested
    @DisplayName("Role Creation Tests")
    class RoleCreationTests {
        @Test
        @DisplayName("When creating a role, return status CREATED with correct data")
        public void createRoleTest() {
            ResponseEntity<MessageResponse> response = roleController.createRole(role, bindingResult);
            assertEquals(CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            RoleDTO responseData = (RoleDTO) response.getBody().getData();
            assertEquals(roleDTO.getId(), responseData.getId());
            assertEquals(roleDTO.getName(), responseData.getName());
        }
    }

    @Nested
    @DisplayName("Role Update Tests")
    class RoleUpdateTests {
        @Test
        @DisplayName("When updating a role, return status OK with updated data")
        public void updateRoleTest() {
            ResponseEntity<MessageResponse> response = roleController.updateRole(1L, role, bindingResult);
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            RoleDTO responseData = (RoleDTO) response.getBody().getData();
            assertEquals(roleDTO.getId(), responseData.getId());
            assertEquals(roleDTO.getName(), responseData.getName());
        }
    }

    @Nested
    @DisplayName("Role Deletion Tests")
    class RoleDeletionTests {
        @Test
        @DisplayName("When deleting a role, return status NO_CONTENT")
        public void deleteRoleTest() {
            ResponseEntity<?> response = roleController.deleteRole(1L);
            assertEquals(NO_CONTENT, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {
        @Test
        @DisplayName("When creating a role with errors, return status BAD_REQUEST")
        public void createRoleWithErrors() {
            when(bindingResult.hasErrors()).thenReturn(true);
            ResponseEntity<MessageResponse> response = roleController.createRole(role, bindingResult);
            assertEquals(BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
        }
    }
}
