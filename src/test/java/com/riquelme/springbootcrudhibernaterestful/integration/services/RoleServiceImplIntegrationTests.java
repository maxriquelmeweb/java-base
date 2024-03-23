package com.riquelme.springbootcrudhibernaterestful.integration.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.RoleRepository;
import com.riquelme.springbootcrudhibernaterestful.services.RoleServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplIntegrationTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void whenFindAll_thenReturnRoleList() {
        Role role1 = new Role(1L, "Admin");
        Role role2 = new Role(2L, "User");
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        List<Role> roles = roleService.findAll();

        assertNotNull(roles);
        assertEquals(2, roles.size());
        verify(roleRepository).findAll();
    }

    @Test
    void whenFindById_thenReturnRole() {
        Role role = new Role(1L, "Admin");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Role foundRole = roleService.findById(1L);

        assertNotNull(foundRole);
        assertEquals("Admin", foundRole.getName());
    }

    @Test
    void whenFindById_thenThrowResourceNotFoundException() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roleService.findById(1L));
    }

    @Test
    @Transactional
    void whenSave_thenPersistRole() {
        Role role = new Role(null, "New Role");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role savedRole = roleService.save(role);

        assertNotNull(savedRole);
        assertEquals("New Role", savedRole.getName());
    }

    @Test
    @Transactional
    void whenUpdate_thenUpdateRoleDetails() {
        Role existingRole = new Role(1L, "Admin");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        Role updatedRole = new Role(1L, "Updated Role");
        Role resultRole = roleService.update(1L, updatedRole);

        assertNotNull(resultRole);
        assertEquals("Updated Role", resultRole.getName());
    }

    @Test
    @Transactional
    void whenDeleteById_thenRoleShouldBeDeleted() {
        Role role = new Role(1L, "Admin");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        roleService.deleteById(1L);

        verify(roleRepository).delete(role);
    }

    @Test
    void whenExistsByName_thenReturnTrueOrFalse() {
        when(roleRepository.existsByName("Admin")).thenReturn(true);
        boolean exists = roleService.existsByName("Admin");

        assertTrue(exists);
    }
}
