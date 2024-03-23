package com.riquelme.springbootcrudhibernaterestful.unit.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.riquelme.springbootcrudhibernaterestful.controllers.UserController;
import com.riquelme.springbootcrudhibernaterestful.dtos.RoleIdsDTO;
import com.riquelme.springbootcrudhibernaterestful.dtos.UserDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.UserRepository;
import com.riquelme.springbootcrudhibernaterestful.responses.MessageResponse;
import com.riquelme.springbootcrudhibernaterestful.services.UserService;

@SpringBootTest
public class UserControllerTests {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDTO userDTO;
    private List<UserDTO> userDTOList;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John", "Doe", "john.doe@example.com", "44e4e4e", true);
        userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com", true, null);
        userDTOList = Arrays.asList(userDTO);
        List<User> userList = userDTOList.stream()
                .map(dto -> new User(dto.getId(), dto.getName(), dto.getLastname(), dto.getEmail(), "password",
                        dto.getActive()))
                .collect(Collectors.toList());
        when(userService.findAll()).thenReturn(userList);
        when(userService.findById(1L)).thenReturn(userList.get(0));
        when(userService.save(any(User.class))).thenReturn(userList.get(0));
        when(userService.update(eq(1L), any(User.class))).thenReturn(userList.get(0));
        when(bindingResult.hasErrors()).thenReturn(false);
    }

    @Nested
    class GetUsersTests {
        @Test
        public void getUsersTest() {
            ResponseEntity<MessageResponse> response = userController.getUsers();
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            List<?> responseData = (List<?>) response.getBody().getData();
            assertEquals(userDTOList.size(), responseData.size());
            assertEquals(userDTOList.get(0).getName(), ((UserDTO) responseData.get(0)).getName());
        }

        @Test
        public void getUserTest() {
            ResponseEntity<MessageResponse> response = userController.getUser(1L);
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(userDTO.toString(), response.getBody().getData().toString());
        }
    }

    @Nested
    class UserModificationTests {
        @Test
        public void createUserTest() {
            ResponseEntity<MessageResponse> response = userController.createUser(user, bindingResult);
            assertEquals(CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(userDTO.toString(), response.getBody().getData().toString());
        }

        @Test
        public void updateUserTest() {
            ResponseEntity<MessageResponse> response = userController.updateUser(1L, user, bindingResult);
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(userDTO.toString(), response.getBody().getData().toString());
        }

        @Test
        public void deleteUserTest() {
            ResponseEntity<?> response = userController.deleteUser(1L);
            assertEquals(NO_CONTENT, response.getStatusCode());
        }
    }

    @Nested
    class RoleManagementTests {

        @Test
        void whenAddRolesToUser_thenReturnsUpdatedUser() {
            Long userId = 1L;
            RoleIdsDTO roleIdsDTO = new RoleIdsDTO();
            roleIdsDTO.setRoleIds(new HashSet<>(Arrays.asList(1L, 2L)));

            when(userService.addRolesToUser(eq(userId), anySet())).thenReturn(user);

            ResponseEntity<MessageResponse> response = userController.addRolesToUser(userId, roleIdsDTO);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getData());
        }

        @Test
        void whenRemoveRolesFromUser_thenReturnsUpdatedUser() {
            Long userId = 1L;
            RoleIdsDTO roleIdsDTO = new RoleIdsDTO();
            roleIdsDTO.setRoleIds(new HashSet<>(Arrays.asList(1L, 2L)));

            when(userService.removeRolesFromUser(eq(userId), anySet())).thenReturn(user);

            ResponseEntity<MessageResponse> response = userController.removeRolesFromUser(userId, roleIdsDTO);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody().getData());
        }

        @Test
        void whenAddRolesToUserWithInvalidUserId_thenHandleError() {
            Long invalidUserId = 999L; // Asumiendo que este ID es inválido
            RoleIdsDTO roleIdsDTO = new RoleIdsDTO();
            roleIdsDTO.setRoleIds(new HashSet<>(Arrays.asList(1L, 2L)));

            // Asegúrate de que el usuario es nulo o lanza una excepción adecuada para
            // simular un usuario no encontrado.
            when(userService.addRolesToUser(eq(invalidUserId), anySet()))
                    .thenThrow(new ResourceNotFoundException("user.error.notfound"));

            // Intenta realizar la llamada al controlador y espera la excepción o una
            // respuesta adecuada.
            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                userController.addRolesToUser(invalidUserId, roleIdsDTO);
            });

            // Verifica que se lanzó la excepción correcta
            assertNotNull(exception);
            assertEquals(exception.getMessage(), new ResourceNotFoundException("user.error.notfound").getMessage());
        }
    }
}
