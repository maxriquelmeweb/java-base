package com.riquelme.springbootcrudhibernaterestful.services;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.RoleRepository;
import com.riquelme.springbootcrudhibernaterestful.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user.error.notfound"));
    }

    @Transactional
    @Override
    public User save(User user) {
        // Buscar el rol "USER" por ID o nombre. Asumimos que el rol con ID 1 es "USER".
        Role userRole = roleRepository.findById(1L)
                .orElseThrow(() -> new ResourceNotFoundException("role.error.notfound"));

        // Asignar el rol al nuevo usuario
        user.setRoles(new HashSet<>(Arrays.asList(userRole)));

        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(Long id, User user) {
        User userDb = findById(id);
        userDb.setName(user.getName());
        userDb.setLastname(user.getLastname());
        userDb.setEmail(user.getEmail());
        userDb.setActive(user.getActive());
        if (user.getPassword() != null) {
            userDb.setPassword(user.getPassword());
        }
        userDb.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(userDb);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        User userDb = findById(id);
        userRepository.delete(userDb);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User addRolesToUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user.error.notfound"));

        Set<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("role.error.notfound")))
                .collect(Collectors.toSet());

        // Añadir nuevos roles al usuario
        user.getRoles().addAll(roles);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User removeRolesFromUser(Long userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user.error.notfound"));

        Set<Role> rolesToRemove = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new ResourceNotFoundException("role.error.notfound")))
                .collect(Collectors.toSet());

        // Verificar que todos los roles a remover realmente pertenecen al usuario
        rolesToRemove.forEach(role -> {
            if (!user.getRoles().contains(role)) {
                throw new IllegalArgumentException();
            }
        });
 
        // Quitar los roles especificados del usuario
        user.getRoles().removeAll(rolesToRemove);
        return userRepository.save(user);
    }
}
