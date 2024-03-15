package com.riquelme.springbootcrudhibernaterestful.dtos.messageResponse;

import com.riquelme.springbootcrudhibernaterestful.responses.SuccessResponse;

public class MessageSuccessDTO implements MessageResponse{
    private SuccessResponse success;
    private Object data;

    public MessageSuccessDTO(String message, Object data) {
        this.success = new SuccessResponse(message);
        this.data = data;
    }

    public String getSuccess() {
        return success.getSuccess();
    }

    public void setSuccess(SuccessResponse success) {
        this.success = success;
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
