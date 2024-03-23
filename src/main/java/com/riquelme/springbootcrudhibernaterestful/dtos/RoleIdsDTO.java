package com.riquelme.springbootcrudhibernaterestful.dtos;

import java.util.Set;

public class RoleIdsDTO {
    private Set<Long> roleIds;

    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }
}