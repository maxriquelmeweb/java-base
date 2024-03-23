package com.riquelme.springbootcrudhibernaterestful.services;

import java.util.List;
import java.util.Set;

import com.riquelme.springbootcrudhibernaterestful.entities.User;

public interface UserService {
  List<User> findAll();

  User findById(Long id);

  User save(User user);

  User update(Long id, User user);

  void deleteById(Long id);

  boolean existsByEmail(String email);

  User addRolesToUser(Long userId, Set<Long> roleIds);

  User removeRolesFromUser(Long userId, Set<Long> roleIds);
}
