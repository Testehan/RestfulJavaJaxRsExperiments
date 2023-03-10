package com.testehan.openliberty.chpt3;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

import java.io.InputStream;

@Path("customers")
public interface CustomerResource {

    @POST
    @Consumes("application/xml")
    Response createCustomer(InputStream is);

    @GET
    @Path("{id}")
    @Produces("application/xml")
    StreamingOutput getCustomer(@PathParam("id") int id);

    // chapter 4
    @GET
    @Path("{firstname}-{lastname}")
    @Produces("application/xml")
    StreamingOutput getCustomerByFirstAndLastName(@PathParam("firstname") String first,
                                                  @PathParam("lastname") String last);


    @GET
    @Path("all")    // chapter 4 - You would not do this /all normally, this is for demo purposes
    @Produces("application/xml")
    StreamingOutput getAllCustomers();

    @PUT
    @Path("{id}")
    @Consumes("application/xml")
    void updateCustomer(@PathParam("id") int id, InputStream is);
}
