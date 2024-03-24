package com.riquelme.springbootcrudhibernaterestful.exceptions.role;

import com.riquelme.springbootcrudhibernaterestful.exceptions.BaseException;

public class RoleNameAlreadyExistsException extends BaseException {

    public RoleNameAlreadyExistsException(String messageKey) {
        super(messageKey);
    }
}

