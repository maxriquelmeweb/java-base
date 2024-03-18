package com.riquelme.springbootcrudhibernaterestful.services;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riquelme.springbootcrudhibernaterestful.entities.User;
import com.riquelme.springbootcrudhibernaterestful.errors.UserNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(Long id, User user) {
        User userDb = findById(id);
        userDb.setName(user.getName());
        userDb.setLastname(user.getLastname());
        userDb.setEmail(user.getEmail());
        userDb.setActive(user.getActive());
        userDb.setCreated_at(user.getCreated_at());
        userDb.setUpdated_at(new Date());
        return userRepository.save(userDb);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        User userDb = findById(id);
        userRepository.delete(userDb);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
