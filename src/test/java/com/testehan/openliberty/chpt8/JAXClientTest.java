package com.testehan.openliberty.chpt8;

import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

public class JAXClientTest {
    @Test
    public void test() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/postreturncustomer");
        Response response = target.request().post(Entity.xml(new Customer("Bill Burke")));
        response.close();

        Customer customer = target.request().get(Customer.class);
        System.out.println(customer);
        client.close();
    }

    @Test
    public void formExample(){
        // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/beanparam
        // you should see in the server log something like:
        // "Created a new car with model=Ferrari Enzo and the year=2012"

        Client client = ClientBuilder.newClient();

        Form form = new Form().param("model", "Ferrari Enzo")
                              .param("year", "2012");
        Response response = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/beanparam")
                                  .request()
                                  .post(Entity.form(form));
        response.close();
    }

    @Test
    // example showcasing an invocation object that represents an entire HTTP request (without it being invoked..)
    // invocation only happens when the invoke() method is called.
    public void multipleInvocations() throws InterruptedException {
        Client client = ClientBuilder.newClient();
        Invocation generateReport = client.target("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/allheaders")
                .request()
                .accept("text/html")
                .buildGet();
        for (int i =0; i<10; i++) {
            generateReport.invoke();
            Thread.sleep(3000);
        }
    }

}
