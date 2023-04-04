package com.testehan.openliberty.chpt9;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

/*

Here, we have three methods that all service the same URI but produce different data
formats. JAX-RS can pick one of these methods based on what is in the Accept header.

*/

@Path("chpt9/customers")
public class Chpt9CustomerResource {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt9/customers/1
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public String getCustomerXml(@PathParam("id") int id) {
        System.out.println("Inside CustomerResource in getCustomerXml");
        return "application/xml";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt9/customers/1
    @GET
    @Path("{id}")
    @Produces("text/plain")
    public String getCustomerText(@PathParam("id") int id) {
        System.out.println("Inside CustomerResource in getCustomerText");
        return "text/plain";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt9/customers/1
    @GET
    @Path("{id}")
    @Produces("application/json")
    public String getCustomerJson(@PathParam("id") int id) {
        System.out.println("Inside CustomerResource in getCustomerJson");
        return "application/json";
    }
}
