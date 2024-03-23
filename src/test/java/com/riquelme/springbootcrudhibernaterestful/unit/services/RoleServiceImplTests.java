package com.riquelme.springbootcrudhibernaterestful.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.RoleRepository;
import com.riquelme.springbootcrudhibernaterestful.services.RoleServiceImpl;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(1L, "Admin");
    }

    @Test
    void whenFindAll_thenReturnRoleList() {
        Role role2 = new Role(2L, "User");
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role, role2));

        List<Role> roles = roleService.findAll();

        assertNotNull(roles);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(role) && roles.contains(role2));
    }

    @Test
    void whenFindById_thenReturnRole() {
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
    void whenSave_thenPersistRole() {
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role savedRole = roleService.save(role);

        assertNotNull(savedRole);
        assertEquals("Admin", savedRole.getName());
    }

    @Test
    void whenUpdate_thenUpdateRoleDetails() {
        Role updatedRole = new Role(1L, "UpdatedRole");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);

        Role result = roleService.update(1L, updatedRole);

        assertNotNull(result);
        assertEquals("UpdatedRole", result.getName());
    }

    @Test
    void whenDeleteById_thenRoleShouldBeDeleted() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        doNothing().when(roleRepository).delete(role);

        roleService.deleteById(1L);

        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    void whenExistsByName_thenReturnTrueOrFalse() {
        when(roleRepository.existsByName("Admin")).thenReturn(true);

        boolean exists = roleService.existsByName("Admin");

        assertTrue(exists);
    }
}
