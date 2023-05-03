package com.testehan.openliberty.chpt10;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("chpt10")
public class Cpt10LinkService {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt10/uri
    // example of a method that uses the UriBuilder to build a URI that could be used in HATEOAS apps
    @GET
    @Path("uri")
    public String getUri() {

        UriBuilder builder = UriBuilder.fromPath("/customers/{id}");
        builder.scheme("http")
                .host("{hostname}")
                .queryParam("param","value");

//         In this code block, we have defined a URI pattern that looks like this:
//        http://{hostname}/customers/{id}?param=value

        UriBuilder clone = builder.clone();     // we call clone because we want to reuse the builder
        URI uri = clone.build("example.com", "333");

        // or

        Map<String, Object> map = new HashMap<>();
        map.put("hostname", "example.com");
        map.put("id", 333);
        UriBuilder clone2 = builder.clone();
        URI uri2 = clone2.buildFromMap(map);

        // or

        String original = "http://{host}/{id}";
        String newTemplate = UriBuilder.fromUri(original)
                .resolveTemplate("host", "localhost")
                .resolveTemplate("id",12)
                .toTemplate();

        return uri.toString() + " \n" + uri2.toString() + " \n" + newTemplate;
    }


    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt10/navigationnext
    // For example, let’s say you have a JAX-RS service that exposes the customers in a customer
    //  database. Instead of having a base URI that returns all customers in a document, you
    //  want to embed previous and next links so that you can navigate through subsections
    //  of the database (this is how you would create those links...by obtaining from UriInfo the absolute path and then
    //  adding the query params)
    @GET
    @Path("navigationnext")
    public String getNavigationNext(@Context UriInfo uriInfo) {
        UriBuilder nextLinkBuilder = uriInfo.getAbsolutePathBuilder();
        nextLinkBuilder.queryParam("start", 5);
        nextLinkBuilder.queryParam("size", 10);
        URI next = nextLinkBuilder.build();

        return next.toString();

    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt10/link
    // this is how one would create a link that can be embedded in the headers
    @GET
    @Path("link")
    public Response getLink() {
        Link link = Link.fromUri("http://{host}/root/customers/{id}")
                .rel("update").type("text/plain")
                .build("localhost", "1234");

        System.out.println(link.toString());    //<http://localhost/root/customers/1234>; rel="update"; type="text/plain"

        Response response = Response.noContent()
                .links(link)
                .build();
        return response;
    }

}
