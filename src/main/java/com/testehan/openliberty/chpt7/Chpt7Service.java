package com.testehan.openliberty.chpt7;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("chpt7")
public class Chpt7Service {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt7/getHarryPotterBook
    @GET
    @Path("getHarryPotterBook")
    @Produces("text/plain")
    public Response getBook() {
        String book = "Harry Potter and the Goblet of Fire";
        Response.ResponseBuilder builder = Response.ok(book);
        builder.language("fr")
               .header("Some-Header", "some value");
        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt7/dummyCookie
    @GET
    @Path("dummyCookie")
    public Response get() {
        NewCookie cookie = new NewCookie("key", "value");
        Response.ResponseBuilder builder = Response.ok("hello", "text/plain");
        return builder.cookie(cookie).build();
    }

    // Here, we’re telling the client that the thing we want to delete is already gone (410).
    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt7/delete
    @DELETE
    @Path("delete")
    public Response delete() {
        return Response.status(Response.Status.GONE).build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt7/listofstrings
    @GET
    @Path("listofstrings")
    @Produces("text/plain")
    public Response getCustomerList() {
        List<String> list = new ArrayList<>();
        list.add("abc");
        list.add("def");
        GenericEntity entity = new GenericEntity<List<String>>(list){};
        return Response.ok(entity).build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt7/webapplicationexception
    // when you open the link in the browser, you will see a 404
    @GET
    @Path("webapplicationexception")
    @Produces("application/xml")
    public void getWebApplicationException() {
        String cust = null; //findCustomer(id);
        if (cust == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt7/notFoundException
    // when you open the link in the browser, you will see a 404
    @GET
    @Path("notFoundException")
    @Produces("application/xml")
    public void getNotFoundException() {
        // this inherits from WebApplicationException, but you don't need to specify the status code
        throw new NotFoundException();
    }


    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt7/illegalargumentexception
    @GET
    @Path("illegalargumentexception")
    @Produces("application/xml")
    public void getEntityNotFoundException() {
        String cust = null; //findCustomer(id);
        if (cust == null) {
            throw new IllegalArgumentException();
        }
    }

}
