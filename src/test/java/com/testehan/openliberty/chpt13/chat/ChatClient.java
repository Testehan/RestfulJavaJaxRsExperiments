package com.testehan.openliberty.chpt13.chat;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.InvocationCallback;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatClient {
// if you run this, and then send from postman a message via "Post Chat"..then you will see the message sent from Postman
// in the console of this app. Or you can try and run this method twice and see what happens..


    public static void main(String[] args) throws IOException {
        String name = "Dan";

        final Client client = new ResteasyClientBuilderImpl().connectionPoolSize(3).build();
        WebTarget target = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/chat");

        target.request().async().get(new InvocationCallback<Response>()
        {
            @Override
            public void completed(Response response)
            {
                Link next = response.getLink("next");
                String message = response.readEntity(String.class);
                System.out.println();
                System.out.print(message);// + "\r");
                System.out.println();
                System.out.print("> ");
                client.target(next).request().async().get(this);
            }
            @Override
            public void failed(Throwable throwable)
            {
                System.err.println("FAILURE!");
            }
        });


        while (true)
        {
            System.out.print("> ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String message = br.readLine();
            target.request().post(Entity.text(name + ": " + message));
        }
    }
}
