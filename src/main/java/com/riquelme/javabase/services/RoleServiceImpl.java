package com.riquelme.javabase.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riquelme.javabase.dtos.RoleDTO;
import com.riquelme.javabase.entities.Role;
import com.riquelme.javabase.exceptions.ExistsException;
import com.riquelme.javabase.exceptions.NotFoundException;
import com.riquelme.javabase.repositories.RoleRepository;
import com.riquelme.javabase.util.EntityDtoMapper;

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
        return roleRepository.findAll().stream()
                .map(role -> entityDtoMapper.convertToDTO(role, RoleDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("role.notFound.message"));
        return entityDtoMapper.convertToDTO(role, RoleDTO.class);
    }

    @Transactional
    @Override
    public RoleDTO save(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new ExistsException("role.existsByNameRole.message");
        }
        return entityDtoMapper.convertToDTO(roleRepository.save(role), RoleDTO.class);
    }

    @Transactional
    @Override
    public RoleDTO update(Long id, Role role) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("role.notFound.message"));
        if (!role.getName().equals(existingRole.getName()) && roleRepository.existsByName(role.getName())) {
            throw new ExistsException("role.existsByNameRole.message");
        }
        existingRole.setName(role.getName());
        return entityDtoMapper.convertToDTO(roleRepository.save(existingRole), RoleDTO.class);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new NotFoundException("role.notFound.message");
        }
        roleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByName(String role) {
        return roleRepository.existsByName(role);
    }
}
