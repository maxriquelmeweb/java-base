package com.riquelme.springbootcrudhibernaterestful.services;

import java.util.List;

import com.riquelme.springbootcrudhibernaterestful.entities.User;

public interface UserService {
  List<User> findAll();

  User findById(Long id);

  User save(User user);

  User update(Long id, User user);

  void deleteById(Long id);

  boolean existsByEmail(String email);
}
