package com.riquelme.javabase.responses;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageResponseImpl implements MessageResponse {

    private MessageSource messageSource;
    private String message;
    private Object data;

    public MessageResponseImpl(MessageSource messageSource, String messageKey) {
        this.messageSource = messageSource;
        this.message = getMessage(messageKey);
    }

    public MessageResponseImpl(MessageSource messageSource, String messageKey, Object data) {
        this(messageSource, messageKey);
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
        return messageSource.getMessage(messageKey, null, "{error.default}",
                LocaleContextHolder.getLocale());
    }
}