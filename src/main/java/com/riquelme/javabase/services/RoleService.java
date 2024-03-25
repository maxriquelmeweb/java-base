package com.riquelme.javabase.services;

import java.util.List;

import com.riquelme.javabase.dtos.RoleDTO;
import com.riquelme.javabase.entities.Role;

public interface RoleService {
  List<RoleDTO> findAll();

  RoleDTO findById(Long id);

  RoleDTO save(Role role);

  RoleDTO update(Long id, Role role);

  void deleteById(Long id);

  boolean existsByName(String role);
}
