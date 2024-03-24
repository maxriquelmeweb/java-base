package com.riquelme.springbootcrudhibernaterestful.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riquelme.springbootcrudhibernaterestful.dtos.RoleDTO;
import com.riquelme.springbootcrudhibernaterestful.entities.Role;
import com.riquelme.springbootcrudhibernaterestful.exceptions.role.RoleNameAlreadyExistsException;
import com.riquelme.springbootcrudhibernaterestful.exceptions.role.RoleNotFoundException;
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
        List<Role> roles = roleRepository.findAll();
        return roles.stream()
                .map(role -> entityDtoMapper.convertToDTO(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public RoleDTO findById(Long id) throws RoleNotFoundException {
        Role roleDb = roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("role.notfound.message"));
        return entityDtoMapper.convertToDTO(roleDb, RoleDTO.class);
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
                .orElseThrow(() -> new RoleNotFoundException("role.notfound.message"));
        if (roleRepository.existsByName(role.getName())) {
            throw new RoleNameAlreadyExistsException("role.existsByNameRole.message");
        }
        roleDb.setName(role.getName());
        Role updateRole = roleRepository.save(roleDb);
        return entityDtoMapper.convertToDTO(updateRole, RoleDTO.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RoleNotFoundException("role.notfound.message");
        }
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByName(String role) {
        return roleRepository.existsByName(role);
    }
}
