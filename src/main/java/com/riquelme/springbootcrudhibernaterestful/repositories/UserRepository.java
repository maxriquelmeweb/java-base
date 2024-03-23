package com.riquelme.springbootcrudhibernaterestful.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.riquelme.springbootcrudhibernaterestful.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsByEmail(String email);

    List<User> findAll();
}
