package com.testehan.openliberty.chpt14;

import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

@Path("chpt14")
public class Chpt14Resource {

    protected int defaultPageSize = 5;
    @Context
    public void setServletContext(ServletContext context) {
        String size = context.getInitParameter("max-customers-size");
        if (size != null) {
            defaultPageSize = Integer.parseInt(size);
        }
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt14/configvalue
    @GET
    @Path("configvalue")
    @Produces("text/plain")
    public Response getValueConfigured() throws InterruptedException {
        Response.ResponseBuilder builder = Response.ok("Value from web.xml config is " + defaultPageSize);
        return builder.build();
    }
}
