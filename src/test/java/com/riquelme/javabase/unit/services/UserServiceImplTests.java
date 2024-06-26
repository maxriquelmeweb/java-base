package com.riquelme.javabase.unit.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.riquelme.javabase.dtos.UserDTO;
import com.riquelme.javabase.entities.Role;
import com.riquelme.javabase.entities.User;
import com.riquelme.javabase.repositories.RoleRepository;
import com.riquelme.javabase.repositories.UserRepository;
import com.riquelme.javabase.services.UserServiceImpl;
import com.riquelme.javabase.util.EntityDtoMapper;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EntityDtoMapper entityDtoMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;
    private User user2;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "Doe", "john.doe@example.com", "password", true);
        user2 = new User(2L, "Jane", "Doe", "jane.doe@example.com", "password", true);
        userDTO = new UserDTO(user.getId(), user.getName(), user.getLastname(), user.getEmail(), user.getActive(),
                new HashSet<>());
    }

    @Test
    @DisplayName("Find all users returns a list of user DTOs")
    void whenFindAll_thenReturnUserDTOList() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user, user2));
        when(entityDtoMapper.convertToDTO(any(User.class), eq(UserDTO.class)))
                .thenAnswer(invocation -> {
                    User argUser = invocation.getArgument(0);
                    return new UserDTO(argUser.getId(), argUser.getName(), argUser.getLastname(),
                            argUser.getEmail(), argUser.getActive(), new HashSet<>());
                });

        List<UserDTO> usersDTO = userService.findAll();

        assertNotNull(usersDTO);
        assertEquals(2, usersDTO.size());
        assertTrue(usersDTO.stream().anyMatch(dto -> dto.getEmail().equals(user.getEmail())));
        assertTrue(usersDTO.stream().anyMatch(dto -> dto.getEmail().equals(user2.getEmail())));
    }

    @Test
    @DisplayName("Find user by ID returns the correct user DTO")
    void whenFindById_thenReturnUserDTO() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(entityDtoMapper.convertToDTO(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO foundUserDTO = userService.findById(1L);

        assertNotNull(foundUserDTO);
        assertEquals(userDTO.getName(), foundUserDTO.getName());
    }

    @Test
    @DisplayName("Save user persists the user and returns its DTO")
    void whenSave_thenPersistUserAndReturnUserDTO() {
        Role userRole = new Role(1L, "USER");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(userRole));

        User newUser = new User(null, "New User", "Lastname", "newuser@example.com", "password123", true);
        newUser.setRoles(new HashSet<>(Arrays.asList(userRole)));

        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(entityDtoMapper.convertToDTO(any(User.class), eq(UserDTO.class))).thenReturn(userDTO);

        UserDTO savedUserDTO = userService.save(newUser);

        assertNotNull(savedUserDTO);
        assertEquals(userDTO.getName(), savedUserDTO.getName());
    }

    @Test
    @DisplayName("Update user changes user details")
    void whenUpdate_thenUpdateUserDetailsAndReturnUserDTO() {
        User updatedUser = new User(1L, "John Updated", "Doe Updated", "john.updated@example.com", "newpassword",
                false);
        UserDTO updatedUserDTO = new UserDTO(updatedUser.getId(), updatedUser.getName(), updatedUser.getLastname(),
                updatedUser.getEmail(), updatedUser.getActive(), new HashSet<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(entityDtoMapper.convertToDTO(updatedUser, UserDTO.class)).thenReturn(updatedUserDTO);

        UserDTO resultDTO = userService.update(1L, updatedUser);

        assertNotNull(resultDTO);
        assertEquals(updatedUserDTO.getEmail(), resultDTO.getEmail());
    }

    @Test
    @DisplayName("Delete user by ID removes the user")
    void whenDeleteById_thenUserShouldBeDeleted() {
        Long id = 1L;
        when(userRepository.existsById(id)).thenReturn(true);
        userService.deleteById(id);
        verify(userRepository).deleteById(id);
    }

    @Test
    @DisplayName("Check user existence by email")
    void whenExistsByEmail_thenReturnTrueOrFalse() {
        when(userRepository.existsByEmail("john.doe@example.com")).thenReturn(true);
        boolean exists = userService.existsByEmail("john.doe@example.com");
        assertTrue(exists);
    }
}
