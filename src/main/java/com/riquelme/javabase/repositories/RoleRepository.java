package com.riquelme.javabase.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.riquelme.javabase.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
   boolean existsByName(String role);

   List<Role> findAll();
}
