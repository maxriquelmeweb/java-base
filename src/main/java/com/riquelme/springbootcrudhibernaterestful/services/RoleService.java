package com.riquelme.springbootcrudhibernaterestful.services;

import java.util.List;

import com.riquelme.springbootcrudhibernaterestful.dtos.RoleDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.Role;

public interface RoleService {
  List<RoleDTO> findAll();

  RoleDTO findById(Long id);

  RoleDTO save(Role role);

  RoleDTO update(Long id, Role role);

  void deleteById(Long id);

  boolean existsByName(String role);
}
