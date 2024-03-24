package com.riquelme.springbootcrudhibernaterestful.exceptions.role;

import com.riquelme.springbootcrudhibernaterestful.exceptions.BaseException;

public class DefaultRoleModificationException extends BaseException {
    public DefaultRoleModificationException(String messageKey) {
        super(messageKey);
    }
}
