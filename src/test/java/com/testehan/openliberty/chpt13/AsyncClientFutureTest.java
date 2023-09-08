package com.testehan.openliberty.chpt13;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.ResponseProcessingException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncClientFutureTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        System.out.println("Starting Async Test Future");
        Client client = ClientBuilder.newClient();

        Future<Response> future1 = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/firstletter")
                .request()
                .async().get();

        Future<Response> future2 = null;
        try {
            future2 = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/secondletter")
                    .request()
                    .async().get();
        } catch (Throwable ignored) {
            ignored.printStackTrace();
        }

        // block until complete
        Response res1 = future1.get();
        try {
            String result1 = res1.readEntity(String.class);
            System.out.println(result1);
        } catch (Throwable ignored) {
            ignored.printStackTrace();
        } finally {
            res1.close();
        }

        // if we successfully executed 2nd request
        Response res2 = null;
        if (future2 != null) {
            // Wait 5 seconds
            try {
                res2 = future2.get(5, TimeUnit.SECONDS);
                String result2 = res2.readEntity(String.class);
                System.out.println(result2);
            } catch (TimeoutException timeout) {
                System.err.println("request timed out");
            } catch (InterruptedException ie) {
                System.err.println("Request was interrupted");
            } catch (ExecutionException ee) {
                Throwable cause = ee.getCause();
                if (cause instanceof WebApplicationException) {
                    WebApplicationException wae = (WebApplicationException) cause;
                    res2.close();
                } else if (cause instanceof ResponseProcessingException) {
                    ResponseProcessingException rpe = (ResponseProcessingException) cause;
                    res2.close();
                } else if (cause instanceof ProcessingException) {
                    // handle processing exception
                } else {
                    // unknown
                }
            }
        }
    }
}
