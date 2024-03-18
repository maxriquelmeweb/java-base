package com.riquelme.springbootcrudhibernaterestful.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.errors.RoleNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAll() {
        return (List<Role>) roleRepository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Transactional
    @Override
    public Role update(Long id, Role role) {
        Role roleDb = findById(id);
        roleDb.setName(role.getName());
        return roleRepository.save(roleDb);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        Role roleDb = findById(id);
        roleRepository.delete(roleDb);
    }
}
