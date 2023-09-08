package com.testehan.openliberty.chpt13;

import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.core.Response;

/*
    The CustomerCallback class implements InvocationCallback with a Response gener‐
    ic parameter. This means JAX-RS will call the completed() method and pass in an
    untouched Response object. If there is a problem sending the request to the server or
    the JAX-RS runtime is unable to create a Response, the failed() method will be invoked
    with the appropriate exception. Otherwise, if there is an HTTP response, then comple
    ted() will be called
*/

public class CustomerCallback implements InvocationCallback<Response> {
    public void completed(Response response) {
        System.out.println("Inside completed method");
        if (response.getStatus() == 200) {
//            Customer cust = response.readEntity(Customer.class);
            String result = response.readEntity(String.class);
            System.out.println("Inside CustomerCallback " + result);
        } else {
            System.err.println("Request error: " + response.getStatus());
        }
    }
    public void failed(Throwable throwable) {
        System.out.println("Inside failed method");
        throwable.printStackTrace();
    }
}
