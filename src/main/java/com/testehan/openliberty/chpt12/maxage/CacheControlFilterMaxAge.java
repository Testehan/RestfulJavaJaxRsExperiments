package com.testehan.openliberty.chpt12.maxage;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;

import java.io.IOException;

public class CacheControlFilterMaxAge implements ContainerResponseFilter {

    private int maxAge;
    public CacheControlFilterMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException
    {
        if (req.getMethod().equals("GET")) {
            res.getHeaders().add("Cache-Control", "max-age="+this.maxAge);
        }
    }
}
