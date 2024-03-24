package com.riquelme.springbootcrudhibernaterestful.exceptions.role;

import com.riquelme.springbootcrudhibernaterestful.exceptions.BaseException;

public class RoleNotFoundException extends BaseException {

    public RoleNotFoundException(String messageKey) {
        super(messageKey);
    }

}