package com.testehan.openliberty.chpt13;

import jakarta.ws.rs.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Response;

import java.util.concurrent.Future;

@Path("chpt13")
public class Chpt13CustomerResource {


    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/firstletter
    @GET
    @Path("firstletter")
    @Produces("text/plain")
    public Response getFirstLetter() throws InterruptedException {

        Thread.sleep(10000);
        String s = "A is first letter";
        Response.ResponseBuilder builder = Response.ok(s);

        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/secondletter
    @GET
    @Path("secondletter")
    @Produces("text/plain")
    public Response getSecondLetter() throws InterruptedException {

        Thread.sleep(15000);
        String s = "B is first letterrrrr";
        Response.ResponseBuilder builder = Response.ok(s);

        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/thirdletter
    // this should just print out in the SERVER console the result of the response from calling that URL
    @GET
    @Path("thirdletter")
    @Produces("text/plain")
    public Response test(){
        System.out.println("Starting Async Test Callback");
        Client client = ClientBuilder.newClient();

        Future<Response> future2 = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/secondletter")
                .request()
                .async().get(new CustomerCallback());


        Response.ResponseBuilder builder = Response.ok("ok");
        return builder.build();
    }


    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/string
    @POST
    @Path("string")
    @Consumes("text/plain")
    public void postString(String str, final @Suspended AsyncResponse response) {
        System.out.println(Thread.currentThread().hashCode());
        System.out.println(str);

        // imagine some complicated stuff going on here..that should run in a background thread
        new Thread() {
            public void run() {
                System.out.println(Thread.currentThread().hashCode());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                response.resume(str);
            }
        }.start();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/quote
    @GET
    @Path("quote")
    @Produces("text/plain")
    public Response getStockQuote(){
        Response.ResponseBuilder builder = Response.ok("125$");
        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/printstock
    // this method just prints the stock value obtained forever.."The preceding continuously polls for a quote using InvocationCallback"
    @GET
    @Path("printstock")
    public void printStock(){
        Client client = ClientBuilder.newClient();
        final WebTarget target = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/quote");
        target.request().async().get(new InvocationCallback<String>() {
            @Override
            public void completed(String quote) {
                System.out.println("RHT: " + quote);
                target.request().async().get(this);
            }

            @Override
            public void failed(Throwable throwable) {

            }
        });
    }
}
