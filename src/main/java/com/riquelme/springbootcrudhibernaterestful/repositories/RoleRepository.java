package com.riquelme.springbootcrudhibernaterestful.repositories;

import org.springframework.data.repository.CrudRepository;

import com.riquelme.springbootcrudhibernaterestful.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
   boolean existsByName(String role);
}
