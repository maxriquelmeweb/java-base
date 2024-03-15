package com.riquelme.springbootcrudhibernaterestful.dtos.messageResponse;

import com.riquelme.springbootcrudhibernaterestful.responses.ErrorResponse;

public class MessageErrorDTO implements MessageResponse {
    private ErrorResponse error;
    private Object data;

    public MessageErrorDTO(String message, Object data) {
        this.error = new ErrorResponse(message);
        this.data = data;
    }

    public String getError() {
        return error.getMessage();
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public void setData(Object data) {
        this.data = data;
    }

}
