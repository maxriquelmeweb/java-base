package com.riquelme.springbootcrudhibernaterestful.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.riquelme.springbootcrudhibernaterestful.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
   boolean existsByName(String role);

   List<Role> findAll();
}
