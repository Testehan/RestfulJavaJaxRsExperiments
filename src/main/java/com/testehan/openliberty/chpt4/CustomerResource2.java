package com.testehan.openliberty.chpt4;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

import java.io.InputStream;

/* this class will do the handling of requests made to
    http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt4/customers/northamerica-db/id/1  (for GET for example)

    Besides the added constructor, another difference in the CustomerResource class from
    previous examples is that it is no longer annotated with @Path. It is no longer a root
    resource in our system; it is a subresource and must not be registered with the JAX-RS
    runtime within an Application class.

    also these resources could have whatever @Path configurations they need on the methods, they don;t have to be the same
    as for CustomerResource class..
 */
public class CustomerResource2 {

    @POST
    @Consumes("application/xml")
    public Response createCustomer(InputStream is) {
        System.out.println("Inside chpt4.CustomerResource2 --> POST");
        return null;
    }
    @GET
    @Path("id/{id}")
    @Produces("application/xml")
    public StreamingOutput getCustomer(@PathParam("id") int id) {
        System.out.println("Inside chpt4.CustomerResource2 --> GET");
        return null;
    }

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    public void updateCustomer(@PathParam("id") int id, InputStream is) {

        System.out.println("Inside chpt4.CustomerResource2 --> PUT");
    }
}
