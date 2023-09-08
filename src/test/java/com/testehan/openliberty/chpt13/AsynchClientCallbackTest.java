package com.testehan.openliberty.chpt13;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Future;

public class AsynchClientCallbackTest {

    // THIS DOES NOT WORK FROM HERE..however it I run it on the server it does work....
    @Test
    public void test()  {
        System.out.println("Starting Async Test Callback");
        Client client = ClientBuilder.newClient();

        Future<Response> future1 = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/firstletter")
                .request()
                .async().get(new CustomerCallback());

        Future<Response> future2 = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/secondletter")
                    .request()
                    .async().get(new CustomerCallback());


//        client.close();

    }
}
