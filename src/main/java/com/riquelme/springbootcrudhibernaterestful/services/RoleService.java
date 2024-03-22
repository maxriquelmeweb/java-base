package com.riquelme.springbootcrudhibernaterestful.services;

import java.util.List;

import com.riquelme.springbootcrudhibernaterestful.entities.Role;

public interface RoleService {
  List<Role> findAll();

  Role findById(Long id);

  Role save(Role role);

  Role update(Long id, Role role);

  void deleteById(Long id);

  boolean existsByName(String role);
}
