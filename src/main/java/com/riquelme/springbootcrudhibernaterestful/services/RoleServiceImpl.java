package com.riquelme.springbootcrudhibernaterestful.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riquelme.springbootcrudhibernaterestful.dtos.RoleDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.exceptions.ResourceNotFoundException;
import com.riquelme.springbootcrudhibernaterestful.repositories.RoleRepository;
import com.riquelme.springbootcrudhibernaterestful.util.EntityDtoMapper;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final EntityDtoMapper entityDtoMapper;

    public RoleServiceImpl(RoleRepository roleRepository, EntityDtoMapper entityDtoMapper) {
        this.roleRepository = roleRepository;
        this.entityDtoMapper = entityDtoMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RoleDTO> findAll() {
        List<Role> roles = (List<Role>) roleRepository.findAll();
        return roles.stream()
                .map(role -> entityDtoMapper.convertToDTO(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role.error.notfound"));
        return entityDtoMapper.convertToDTO(role, RoleDTO.class);
    }

    @Transactional
    @Override
    public RoleDTO save(Role role) {
        Role newRole = roleRepository.save(role);
        return entityDtoMapper.convertToDTO(newRole, RoleDTO.class);
    }

    @Transactional
    @Override
    public RoleDTO update(Long id, Role role) {
        Role roleDb = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role.error.notfound"));
        if (roleRepository.existsByName(role.getName())) {
            throw new ResourceNotFoundException("existsByNameRole.message");
        }
        roleDb.setName(role.getName());
        Role updateRole = roleRepository.save(roleDb);
        return entityDtoMapper.convertToDTO(updateRole, RoleDTO.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("role.error.notfound"));
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByName(String role) {
        return roleRepository.existsByName(role);
    }
}
