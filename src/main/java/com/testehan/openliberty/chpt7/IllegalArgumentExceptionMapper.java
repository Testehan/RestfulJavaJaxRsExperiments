package com.testehan.openliberty.chpt7;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException> {
    @Override
    public Response toResponse(IllegalArgumentException e) {
        System.out.println("Inside IllegalArgumentExceptionMapper because IllegalArgumentException was thrown");
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
