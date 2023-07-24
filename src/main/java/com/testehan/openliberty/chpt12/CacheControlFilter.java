package com.testehan.openliberty.chpt12;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
public class CacheControlFilter implements ContainerResponseFilter {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt11/entitytag
    // response to this GET request will contain that header
    public void filter(ContainerRequestContext req, ContainerResponseContext res) throws IOException
    {
        // commented out because I introduced a new approach that uses the @MaxAge annotation which you can place on a
        // method to provide a custom value for each method
//        if (req.getMethod().equals("GET")) {
//            res.getHeaders().add("Cache-Control", "max-age=100");
//        }
    }
}
