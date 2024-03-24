package com.riquelme.springbootcrudhibernaterestful.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riquelme.springbootcrudhibernaterestful.dtos.UserDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.exceptions.DefaultModificationException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ExistsException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.NotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.RoleRepository;
import com.riquelme.springbootcrudhibernaterestful.repositories.UserRepository;
import com.riquelme.springbootcrudhibernaterestful.util.EntityDtoMapper;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EntityDtoMapper entityDtoMapper;
    private final PasswordEncoder passwordEncoder;
    private static final int DEFAULT_ROL = 1;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            EntityDtoMapper entityDtoMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.entityDtoMapper = entityDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> entityDtoMapper.convertToDTO(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user.notfound.message"));
        return entityDtoMapper.convertToDTO(user, UserDTO.class);
    }

    @Transactional
    @Override
    public UserDTO save(User user) {
        // Buscar el rol "USER" por ID o nombre. Asumimos que el rol con ID 1 es "USER".
        Role userRole = roleRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException("role.notDefaultRole.message"));
        // Hashear la contraseña antes de guardar
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        // Asignar el rol al nuevo usuario
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));
        User newUser = userRepository.save(user);
        return entityDtoMapper.convertToDTO(newUser, UserDTO.class);
    }

    @Transactional
    @Override
    public UserDTO update(Long id, User user) {
        User userDb = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("user.notfound.message"));
        userDb.setName(user.getName());
        userDb.setLastname(user.getLastname());
        userDb.setEmail(user.getEmail());
        userDb.setActive(user.getActive());
        if (user.getPassword() != null) {
            userDb.setPassword(user.getPassword());
        }
        userDb.setUpdatedAt(LocalDateTime.now());
        User updateUser = userRepository.save(userDb);
        return entityDtoMapper.convertToDTO(updateUser, UserDTO.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("user.notfound.message");
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public UserDTO addRolesToUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user.notfound.message"));
        Set<Role> existingRoles = user.getRoles();
        Set<Role> rolesToAdd = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new NotFoundException("role.notfound.message")))
                .filter(newRole -> existingRoles.stream()
                        .noneMatch(existingRole -> existingRole.getId().equals(newRole.getId())))
                .collect(Collectors.toSet());
        if (rolesToAdd.isEmpty()) {
            throw new ExistsException("role.existsByNameRole.message");
        }
        // Añadir nuevos roles al usuario
        user.getRoles().addAll(rolesToAdd);
        User updateUser = userRepository.save(user);
        return entityDtoMapper.convertToDTO(updateUser, UserDTO.class);
    }

    @Override
    @Transactional
    public UserDTO removeRolesFromUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("user.notfound.message"));

        Set<Role> rolesToRemove = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new NotFoundException("role.notfound.message")))
                .collect(Collectors.toSet());

        rolesToRemove.forEach(role -> {
            // Verificar que todos los roles a remover realmente pertenecen al usuario
            if (!user.getRoles().contains(role)) {
                throw new NotFoundException("user.role.notfound.message");
            }
            // No se permite borrar el rol por defecto al usuario
            if (role.getId() == DEFAULT_ROL) {
                throw new DefaultModificationException("role.default.modification.forbidden");
            }
        });

        // Quitar los roles especificados del usuario
        user.getRoles().removeAll(rolesToRemove);

        User updateUser = userRepository.save(user);
        return entityDtoMapper.convertToDTO(updateUser, UserDTO.class);
    }
}
