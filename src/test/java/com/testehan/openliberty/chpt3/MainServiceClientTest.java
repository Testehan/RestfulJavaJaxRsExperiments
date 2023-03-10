package com.testehan.openliberty.chpt3;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

public class MainServiceClientTest {

    @Test
    public void test() {
        Client client = ClientBuilder.newClient();
        try {
            System.out.println("*** Create a new Customer ***");
            final String xml = "<customer>"
                    + "<first-name>Bill</first-name>"
                    + "<last-name>Burke</last-name>"
                    + "<street>256 Clarendon Street</street>"
                    + "<city>Boston</city>"
                    + "<state>MA</state>"
                    + "<zip>02115</zip>"
                    + "<country>USA</country>"
                    + "</customer>";
            Response response = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/customers")
                                      .request()
                                      .post(Entity.xml(xml));
            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed to create");
            }

            String location = response.getLocation().toString();
            System.out.println("Location: " + location);
            response.close();

            System.out.println("*** GET Created Customer **");
            String customer = client.target(location).request().get(String.class);
            System.out.println(customer);
            final String updateCustomer = "<customer>"
                    + "<first-name>William</first-name>"
                    + "<last-name>Burke</last-name>"
                    + "<street>256 Clarendon Street</street>"
                    + "<city>Boston</city>"
                    + "<state>MA</state>"
                    + "<zip>02115</zip>"
                    + "<country>USA</country>"
                    + "</customer>";
            response = client.target(location)
                             .request()
                             .put(Entity.xml(updateCustomer));
            if (response.getStatus() != 204) {
                throw new RuntimeException("Failed to update");
            }
            response.close();

            System.out.println("**** After Update ***");
            customer = client.target(location).request().get(String.class);
            System.out.println(customer);
        } finally {
            client.close();
        }
    }
}
