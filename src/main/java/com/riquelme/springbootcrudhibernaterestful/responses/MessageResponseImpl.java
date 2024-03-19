package com.riquelme.springbootcrudhibernaterestful.responses;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageResponseImpl implements MessageResponse {

    private MessageSource messageSource;
    private String message;
    private Object data;
    private Object[] args;

    public MessageResponseImpl(MessageSource messageSource, String messageKey, Object data, Object[] args) {
        this.messageSource = messageSource;
        this.message = getMessage(messageKey);
        this.data = data;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Object getData() {
        return data;
    }

    private String getMessage(String messageKey) {
        return messageSource.getMessage(messageKey, args, "An unexpected error occurred.",
                LocaleContextHolder.getLocale());
    }
}