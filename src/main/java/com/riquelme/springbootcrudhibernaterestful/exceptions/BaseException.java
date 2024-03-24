package com.riquelme.springbootcrudhibernaterestful.exceptions;

public class BaseException extends RuntimeException {
    protected String messageKey;

    public BaseException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public BaseException(String messageKey, Throwable cause) {
        super(messageKey, cause);
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

}
