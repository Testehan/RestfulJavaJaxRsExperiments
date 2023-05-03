package com.testehan.openliberty.chpt10;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Link;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Path("chpt10customers")
public class CustomersService {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt10customers
    @GET
    @Produces("application/xml")
    public Customers getCustomers(@QueryParam("start") int start,
                                  @QueryParam("size") @DefaultValue("2") int size,
                                  @Context UriInfo uriInfo)     // The UriInfo instance injected with @Context is used to build the URLs that define next and previous link relationships
    {
        // Get the absolute path of the request in the form of a UriBuilder. This includes everything preceding
        // the path (host, port etc) but excludes query parameters. (Dan it seems like in the past the query params were
        // excluded but now are added for some reason..and this causes a multiplication of the quey params)
        // I did not spend more time on this because overall the method works...just that the link references from inside
        // are not working as expected..
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.queryParam("start", "{start}");
        builder.queryParam("size", "{size}");

        ArrayList<Customer> list = new ArrayList<>();
        ArrayList<Link> links = new ArrayList<>();

        int i = 0;
        for (Customer customer : createDummyCustomers())
        {
            if (i >= start && i < start + size)
                list.add(customer);
            i++;
        }

        // next link
        if (start + size < createDummyCustomers().size())
        {
            int next = start + size;
            URI nextUri = builder.clone()
//                            .queryParam("start", next)
//                            .queryParam("size", size)
                            .build(next,size);

            Link nextLink = Link.fromUri(nextUri)
                    .rel("next")
                    .type("application/xml").build();
            links.add(nextLink);
        }
        // previous link
        if (start > 0)
        {
            int previous = start - size;
            if (previous < 0) previous = 0;
            URI previousUri = builder.clone()
//                                .queryParam("start", previous)
//                                .queryParam("size", size)
                                .build(previous,size);
            Link previousLink = Link.fromUri(previousUri)
                    .rel("previous")
                    .type("application/xml").build();
            links.add(previousLink);
        }

        Customers customers = new Customers();
        customers.setCustomers(list);
        customers.setLinks(links);
        return customers;
    }


    private List<Customer> createDummyCustomers() {
        Customer customer = new Customer();
        customer.setId(1);
        customer.setFullname("Bill Burke");

        Customer customer2 = new Customer();
        customer2.setId(2);
        customer2.setFullname("Dan Testehan");

        Customer customer3 = new Customer();
        customer3.setId(3);
        customer3.setFullname("Dan Popescu");

        Customer customer4 = new Customer();
        customer4.setId(4);
        customer4.setFullname("Dan Mihai");

        Customer customer5 = new Customer();
        customer5.setId(5);
        customer5.setFullname("Dan Pestea");

        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);
        customerList.add(customer2);
        customerList.add(customer3);
        customerList.add(customer4);
        customerList.add(customer5);

        return customerList;
    }
}
