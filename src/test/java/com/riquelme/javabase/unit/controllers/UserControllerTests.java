package com.riquelme.javabase.unit.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

import java.util.Arrays;
import java.util.HashSet;
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

import com.riquelme.javabase.controllers.UserController;
import com.riquelme.javabase.dtos.RoleIdsDTO;
import com.riquelme.javabase.dtos.UserDTO;
import com.riquelme.javabase.entities.User;
import com.riquelme.javabase.exceptions.NotFoundException;
import com.riquelme.javabase.repositories.UserRepository;
import com.riquelme.javabase.responses.MessageResponse;
import com.riquelme.javabase.services.UserService;

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
    @DisplayName("Get Users Tests")
    class GetUsersTests {
        @Test
        @DisplayName("When getting all users, return status OK with correct data")
        public void getUsersTest() {
            ResponseEntity<MessageResponse> response = userController.getUsers();
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            List<?> responseData = (List<?>) response.getBody().getData();
            assertEquals(userDTOList.size(), responseData.size());
            assertEquals(userDTOList.get(0).getName(), ((UserDTO) responseData.get(0)).getName());
        }

        @Test
        @DisplayName("When getting a specific user by ID, return status OK with correct data")
        public void getUserTest() {
            ResponseEntity<MessageResponse> response = userController.getUser(1L);
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            UserDTO responseData = (UserDTO) response.getBody().getData();
            assertEquals(userDTO.getId(), responseData.getId());
            assertEquals(userDTO.getName(), responseData.getName());
        }
    }

    @Nested
    @DisplayName("User Creation Tests")
    class UserCreationTests {
        @Test
        @DisplayName("When creating a user, return status CREATED with correct data")
        public void createUserTest() {
            ResponseEntity<MessageResponse> response = userController.createUser(user, bindingResult);
            assertEquals(CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
            UserDTO responseData = (UserDTO) response.getBody().getData();
            assertEquals(userDTO.getId(), responseData.getId());
            assertEquals(userDTO.getName(), responseData.getName());
        }
    }

    @Nested
    @DisplayName("User Update Tests")
    class UserUpdateTests {
        @Test
        @DisplayName("When updating a user, return status OK with updated data")
        public void updateUserTest() {
            ResponseEntity<MessageResponse> response = userController.updateUser(1L, user, bindingResult);
            assertEquals(OK, response.getStatusCode());
            assertNotNull(response.getBody());
            UserDTO responseData = (UserDTO) response.getBody().getData();
            assertEquals(userDTO.getId(), responseData.getId());
            assertEquals(userDTO.getName(), responseData.getName());
        }

        @Test
        @DisplayName("Handle error when updating a user with invalid ID")
        void whenUpdateUserWithInvalidUserId_thenHandleError() {
            Long invalidUserId = 999L; // Assuming this ID is invalid
            when(userService.update(eq(invalidUserId), any(User.class)))
                    .thenThrow(new NotFoundException("User not found"));

            Exception exception = assertThrows(NotFoundException.class,
                    () -> userController.updateUser(invalidUserId, user, bindingResult));
            assertNotNull(exception);
        }
    }

    @Nested
    @DisplayName("User Deletion Tests")
    class UserDeletionTests {
        @Test
        @DisplayName("When deleting a user, return status NO_CONTENT")
        public void deleteUserTest() {
            ResponseEntity<?> response = userController.deleteUser(1L);
            assertEquals(NO_CONTENT, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Role Management Tests")
    class RoleManagementTests {
        @Test
        @DisplayName("Handle error when adding roles to a user with an invalid ID")
        void whenAddRolesToUserWithInvalidUserId_thenHandleError() {
            Long invalidUserId = 999L; // Assuming this ID is invalid
            RoleIdsDTO roleIdsDTO = new RoleIdsDTO();
            roleIdsDTO.setRoleIds(new HashSet<>(Arrays.asList(1L, 2L)));

            when(userService.addRolesToUser(eq(invalidUserId), anySet()))
                    .thenThrow(new NotFoundException("User not found"));

            Exception exception = assertThrows(NotFoundException.class,
                    () -> userController.addRolesToUser(invalidUserId, roleIdsDTO));
            assertNotNull(exception);
        }
    }
}
