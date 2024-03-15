package com.riquelme.springbootcrudhibernaterestful.responses;

public class SuccessResponse {

    private String success;

    public SuccessResponse(String success) {
        this.success = success;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

}
