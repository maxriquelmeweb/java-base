package com.riquelme.springbootcrudhibernaterestful.unit.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.riquelme.springbootcrudhibernaterestful.controllers.RoleController;
import com.riquelme.springbootcrudhibernaterestful.dtos.RoleDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.services.RoleService;

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
        List<Role> roleList = roleDTOList.stream()
                .map(dto -> new Role(dto.getId(), dto.getName()))
                .collect(Collectors.toList());
        when(roleService.findAll()).thenReturn(roleList);
        when(roleService.findById(1L)).thenReturn(roleList.get(0));
        when(roleService.save(any(Role.class))).thenReturn(roleList.get(0));
        when(roleService.update(eq(1L), any(Role.class))).thenReturn(roleList.get(0));
        when(bindingResult.hasErrors()).thenReturn(false);
    }

    @Nested
    class GetRolesTests {
        @Test
        public void getRolesTest() {
            ResponseEntity<MessageResponse> response = roleController.getRoles();
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            List<?> responseData = (List<?>) response.getBody().getData();
            assertEquals(roleDTOList.size(), responseData.size());
            assertEquals(roleDTOList.get(0).getName(), ((RoleDTO) responseData.get(0)).getName());
        }

        @Test
        public void getRoleTest() {
            ResponseEntity<MessageResponse> response = roleController.getRole(1L);
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            RoleDTO responseData = (RoleDTO) response.getBody().getData();
            assertEquals(roleDTO.getName(), responseData.getName());
        }
    }

    @Nested
    class RoleModificationTests {
        @Test
        public void createRoleTest() {
            ResponseEntity<MessageResponse> response = roleController.createRole(role, bindingResult);
            assertEquals(CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(roleDTO.toString(), response.getBody().getData().toString());
        }

        @Test
        public void updateRoleTest() {
            ResponseEntity<MessageResponse> response = roleController.updateRole(1L, role, bindingResult);
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(roleDTO.toString(), response.getBody().getData().toString());
        }

        @Test
        public void deleteRoleTest() {
            ResponseEntity<?> response = roleController.deleteRole(1L);
            assertEquals(NO_CONTENT, response.getStatusCode());
        }
    }
}
