package com.riquelme.springbootcrudhibernaterestful.exceptions.login;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserNotFoundException extends UsernameNotFoundException {
    private final String messageKey;

    public CustomUserNotFoundException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
