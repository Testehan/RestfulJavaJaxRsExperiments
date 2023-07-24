package com.testehan.openliberty.chpt12;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@PreMatching
public class HttpMethodOverride implements ContainerRequestFilter {

    /*
        This HttpMethodOverride filter will run before the HTTP request is matched to a spe‐
        cific JAX-RS method. The ContainerRequestContext parameter passed to the fil
        ter() method provides information about the request like headers, the URI, and so on.
        The filter() method uses the ContainerRequestContext parameter to check the val‐
        ue of the X-Http-Method-Override header. If the header is set in the request, the filter
        overrides the request’s HTTP method by calling ContainerRequestFilter.setMethod().
    */

    //http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt11/updateConcurrently
    // This endpoint expects a PUT...I WILL sent it via a POST, BUT with the header X-Http-Method-Override
    // set on "PUT" ...it should get to that endpoint because in this method, the HTTP method will be set corectly
    public void filter(ContainerRequestContext ctx) throws IOException {
        String methodOverride = ctx.getHeaderString("X-Http-Method-Override");
        if (methodOverride != null){
            System.out.println("I will set the HTTP method to " + methodOverride);
            ctx.setMethod(methodOverride);
        }
    }
}
