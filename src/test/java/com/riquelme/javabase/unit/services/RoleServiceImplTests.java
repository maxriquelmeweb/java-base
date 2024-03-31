package com.riquelme.javabase.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.riquelme.javabase.dtos.RoleDTO;
import com.riquelme.javabase.entities.Role;
import com.riquelme.javabase.exceptions.NotFoundException;
import com.riquelme.javabase.repositories.RoleRepository;
import com.riquelme.javabase.services.RoleServiceImpl;
import com.riquelme.javabase.util.EntityDtoMapper;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTests {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    private Role adminRole;
    private Role userRole;

    @BeforeEach
    void setUp() {
        adminRole = new Role(1L, "Admin");
        userRole = new Role(2L, "User");
    }

    @Test
    @DisplayName("Find all roles returns a list of role DTOs")
    void whenFindAll_thenReturnRoleList() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(adminRole, userRole));
        List<RoleDTO> rolesDTOs = roleService.findAll();
        assertNotNull(rolesDTOs);
        assertEquals(2, rolesDTOs.size());
    }

    @Test
    @DisplayName("Find role by ID returns the correct role DTO")
    void whenFindById_thenReturnRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(adminRole));
        when(entityDtoMapper.convertToDTO(any(Role.class), eq(RoleDTO.class)))
                .thenReturn(new RoleDTO(adminRole.getId(), adminRole.getName()));

        RoleDTO roleDTO = roleService.findById(1L);

        assertNotNull(roleDTO);
        assertEquals(adminRole.getId(), roleDTO.getId());
    }

    @Test
    @DisplayName("Save role persists the role and returns its DTO")
    void whenSave_thenPersistRole() {
        Role newRole = new Role(null, "New Role");
        RoleDTO newRoleDTO = new RoleDTO(1L, "New Role");

        when(roleRepository.save(any(Role.class))).thenReturn(newRole);
        when(entityDtoMapper.convertToDTO(any(Role.class), eq(RoleDTO.class))).thenReturn(newRoleDTO);

        RoleDTO savedRoleDTO = roleService.save(newRole);

        assertNotNull(savedRoleDTO);
        assertEquals("New Role", savedRoleDTO.getName());
    }

    @Test
    @DisplayName("Update role changes role details")
    void whenUpdate_thenUpdateRoleDetails() {
        Role updatedRole = new Role(1L, "UpdatedRole");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(adminRole));
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);
        when(entityDtoMapper.convertToDTO(any(Role.class), eq(RoleDTO.class)))
                .thenReturn(new RoleDTO(updatedRole.getId(), updatedRole.getName()));

        RoleDTO updatedRoleDTO = roleService.update(1L, updatedRole);

        assertNotNull(updatedRoleDTO);
        assertEquals(updatedRole.getName(), updatedRoleDTO.getName());
    }

    @Test
    @DisplayName("Delete role by ID removes the role")
    void whenDeleteById_thenRoleShouldBeDeleted() {
        Long id = 1L;
        when(roleRepository.existsById(id)).thenReturn(true);
        roleService.deleteById(id);
        verify(roleRepository).deleteById(id);
    }

    @Test
    @DisplayName("Delete non-existing role throws NotFoundException")
    void whenDeleteById_withNonExistingRole_thenThrowRoleNotFoundException() {
        Long id = 1L;
        when(roleRepository.existsById(id)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> roleService.deleteById(id));
        verify(roleRepository, never()).deleteById(id);
    }

    @Test
    @DisplayName("Check role existence by name")
    void whenExistsByName_thenReturnTrueOrFalse() {
        when(roleRepository.existsByName("Admin")).thenReturn(true);
        boolean exists = roleService.existsByName("Admin");
        assertTrue(exists);
    }
}
