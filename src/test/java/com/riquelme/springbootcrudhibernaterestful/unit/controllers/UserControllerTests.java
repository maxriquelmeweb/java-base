package com.riquelme.springbootcrudhibernaterestful.unit.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.riquelme.springbootcrudhibernaterestful.controllers.UserController;
import com.riquelme.springbootcrudhibernaterestful.dtos.RoleIdsDTO;
import com.riquelme.springbootcrudhibernaterestful.dtos.UserDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.exceptions.NotFoundException;
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
        userDTO = new UserDTO(1L, "John", "Doe", "john.doe@example.com", true, new HashSet<>());
        userDTOList = Arrays.asList(userDTO);

        when(userService.findAll()).thenReturn(userDTOList);
        when(userService.findById(1L)).thenReturn(userDTO);
        when(userService.save(any(User.class))).thenReturn(userDTO);
        when(userService.update(eq(1L), any(User.class))).thenReturn(userDTO);
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

        // Pruebas para la gestión de roles...

        @Test
        void whenAddRolesToUserWithInvalidUserId_thenHandleError() {
            Long invalidUserId = 999L; // Asumiendo que este ID es inválido
            RoleIdsDTO roleIdsDTO = new RoleIdsDTO();
            roleIdsDTO.setRoleIds(new HashSet<>(Arrays.asList(1L, 2L)));

            when(userService.addRolesToUser(eq(invalidUserId), anySet()))
                    .thenThrow(new NotFoundException("user.notfound.message"));

            Exception exception = assertThrows(NotFoundException.class, () -> {
                userController.addRolesToUser(invalidUserId, roleIdsDTO);
            });

            assertNotNull(exception);
        }
    }
}
