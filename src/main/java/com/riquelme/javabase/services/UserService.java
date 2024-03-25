package com.riquelme.javabase.services;

import java.util.List;
import java.util.Set;

import com.riquelme.javabase.dtos.UserDTO;
import com.riquelme.javabase.entities.User;

public interface UserService {
  List<UserDTO> findAll();

  UserDTO findById(Long id);

  UserDTO save(User user);

  UserDTO update(Long id, User user);

  void deleteById(Long id);

  boolean existsByEmail(String email);

  UserDTO addRolesToUser(Long userId, Set<Long> roleIds);

  UserDTO removeRolesFromUser(Long userId, Set<Long> roleIds);
}
