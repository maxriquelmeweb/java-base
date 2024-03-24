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

import com.riquelme.springbootcrudhibernaterestful.dtos.RoleDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.exceptions.role.RoleNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.RoleRepository;
import com.riquelme.springbootcrudhibernaterestful.services.RoleServiceImpl;
import com.riquelme.springbootcrudhibernaterestful.util.EntityDtoMapper;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(1L, "Admin");
    }

    @Test
    void whenFindAll_thenReturnRoleList() {
        Role role1 = new Role(1L, "Admin");
        Role role2 = new Role(2L, "User");

        when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));

        List<RoleDTO> rolesDTOs = roleService.findAll();

        assertNotNull(rolesDTOs);
        assertEquals(2, rolesDTOs.size());
    }

    @Test
    void whenFindById_thenReturnRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(entityDtoMapper.convertToDTO(any(Role.class), eq(RoleDTO.class)))
                .thenReturn(new RoleDTO(role.getId(), role.getName()));

        RoleDTO roleDTO = roleService.findById(1L);

        assertNotNull(roleDTO);
        assertEquals(role.getId(), roleDTO.getId());
    }

    @Test
    void whenSave_thenPersistRole() {
        Role role = new Role(null, "New Role");
        RoleDTO roleDTO = new RoleDTO(1L, "New Role");

        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(entityDtoMapper.convertToDTO(any(Role.class), eq(RoleDTO.class))).thenReturn(roleDTO);

        RoleDTO savedRoleDTO = roleService.save(role);

        assertNotNull(savedRoleDTO);
        assertEquals("New Role", savedRoleDTO.getName());
    }

    @Test
    void whenUpdate_thenUpdateRoleDetails() {
        Role updatedRole = new Role(1L, "UpdatedRole");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);
        when(entityDtoMapper.convertToDTO(any(Role.class), eq(RoleDTO.class)))
                .thenReturn(new RoleDTO(updatedRole.getId(), updatedRole.getName()));

        RoleDTO updatedRoleDTO = roleService.update(1L, updatedRole);

        assertNotNull(updatedRoleDTO);
        assertEquals(updatedRole.getName(), updatedRoleDTO.getName());
    }

    @Test
    void whenDeleteById_thenRoleShouldBeDeleted() {
        Long id = 1L;
        when(roleRepository.existsById(id)).thenReturn(true);
        roleService.deleteById(id);
        verify(roleRepository).deleteById(id);
    }

    @Test
    void whenDeleteById_withNonExistingRole_thenThrowRoleNotFoundException() {
        Long id = 1L;
        when(roleRepository.existsById(id)).thenReturn(false);
        assertThrows(RoleNotFoundException.class, () -> roleService.deleteById(id));
        verify(roleRepository, never()).deleteById(id);
    }

    @Test
    void whenExistsByName_thenReturnTrueOrFalse() {
        when(roleRepository.existsByName("Admin")).thenReturn(true);

        boolean exists = roleService.existsByName("Admin");

        assertTrue(exists);
    }
}
