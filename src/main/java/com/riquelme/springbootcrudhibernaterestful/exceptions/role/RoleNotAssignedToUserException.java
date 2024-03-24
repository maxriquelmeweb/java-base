package com.riquelme.springbootcrudhibernaterestful.exceptions.role;

import com.riquelme.springbootcrudhibernaterestful.exceptions.BaseException;

public class RoleNotAssignedToUserException extends BaseException {

    public RoleNotAssignedToUserException(String messageKey) {
        super(messageKey);
    }

}
