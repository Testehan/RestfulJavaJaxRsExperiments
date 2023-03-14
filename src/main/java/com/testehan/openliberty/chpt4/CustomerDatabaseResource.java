package com.testehan.openliberty.chpt4;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

/*
    Subresource Locators + Full Dynamic Dispatching

    The CustomerDatabaseResource class is our root resource. It does not service any
    HTTP requests directly. It processes the database identifier part of the URI and locates
    the identified customer database and then uses that object to complete the request.
 */
@Path("chpt4/customers")
public class CustomerDatabaseResource {

    @Path("{database}-db")
    public Object getDatabase(@PathParam("database") String db) {
        // find the instance based on the db parameter..right now i just return an instance of the class that will
        // do the handling of the requests
        Object resource = locateCustomerResource(db);
        return resource;
    }
    protected Object locateCustomerResource(String db) {
        if (db.equals("europe")) {
            return new CustomerResource();
        }
        else if (db.equals("northamerica")) {
            return new CustomerResource2();
        }
        else return null;

    }
}
