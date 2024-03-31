package com.riquelme.javabase.http;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import jakarta.servlet.http.HttpServletRequest;

@Component
@RequestScope
public class HttpRequestOperationStrategyImpl implements HttpRequestOperationStrategy {

    private final HttpServletRequest request;

    public HttpRequestOperationStrategyImpl(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public boolean isUpdateOperation() {
        return "PUT".equalsIgnoreCase(request.getMethod());
    }
}