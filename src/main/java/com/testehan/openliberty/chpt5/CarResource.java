package com.testehan.openliberty.chpt5;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

@Path("chpt5/cars/{maker}")
public class CarResource {

    // this method handles calls like :
    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/i3;color=red/2022
    // the color=red part is obtained from the PathSegment from the matrix parameters
    @GET
    @Path("/{model}/{year}")
    @Produces("image/jpeg")
    public Object getPicture(@PathParam("maker") String maker,
                            @PathParam("model") PathSegment car,
                            @PathParam("year") String year) {
        String carColor = car.getMatrixParameters().getFirst("color");
        System.out.println(maker + " model " + car.getPath() + " has car color is " + carColor + " and is from year " + year);
        return null;
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/programatic/i3;color=red/2022
    // here we obtain URI information in a programatic way
    @GET
    @Path("/programatic/{model}/{year}")
    @Produces("image/jpeg")
    public Object getPicture(@Context UriInfo info) {
        String maker = info.getPathParameters().getFirst("maker");
        PathSegment model = info.getPathSegments().get(4);
        String color = model.getMatrixParameters().getFirst("color");
        PathSegment year = info.getPathSegments().get(5);

        System.out.println("Programatic way " + maker + " model " + model.getPath() + " has car color is " + color + " and is from year " + year.getPath());
        return null;
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/matrixparam/i3;color=red
    // here we obtain URI information in a programatic way
    @GET
    @Path("/matrixparam/{model}")
    @Produces("text/html")
    public Object getPicture(@PathParam("maker") String maker,
                             @PathParam("model") String model,
                             @MatrixParam("color") String color) {

        System.out.println("MatrixParam way " + maker + " model " + model + " has car color is " + color);
        return "IT WORKED";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw?start=0&size=10
    // here we obtain URI information in a programatic way
    @GET
    @Produces("application/xml")
    public String getCustomers(@QueryParam("start") int start,
                               @QueryParam("size") int size) {


        System.out.println("QueryParam way start=" + start + " and size=" + size);
        return "<cars></cars>";
    }

    // or if you need to obtain the start and size in a programatic way
    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/context?start=0&size=10
    @GET
    @Path("/context")
    @Produces("application/xml")
    public String getCustomers(@Context UriInfo info) {
        String start = info.getQueryParameters().getFirst("start");
        String size = info.getQueryParameters().getFirst("size");

        System.out.println("QueryParam  programatic way start=" + start + " and size=" + size);
        return "<cars></cars>";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw
    // I used postman to send a request to this method (saved in Postman with name "Post Using FORM data")
    @POST
    public void createCar(@FormParam("model") String model,
                          @FormParam("year") String year) {

        System.out.println("Created a new car with model=" + model + " and the year="+year);

    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/headers
    @GET
    @Path("/headers")
    @Produces("text/html")
    public String getUserAgentHeader(@HeaderParam("User-Agent") String userAgent) {
        System.out.println("The request was sent from " + userAgent);
        return userAgent;
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/allheaders
    @GET
    @Path("/allheaders")
    @Produces("text/html")
    public String getAllHeaders(@Context HttpHeaders headers) {
        for (String header : headers.getRequestHeaders().keySet())
        {
            System.out.println("This header was set: " + header);
        }
        return "all headers were printed in the console log";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/cookie
    /*
        The use of @CookieParam here makes the JAX-RS provider search all cookie headers for
        the customerId cookie value. It then converts it into an int and injects it into the custId
        parameter.
     */
    @GET
    @Path("/cookie")
    @Produces("text/html")
    public String getCookie(@CookieParam("customerId") String custId) {
        System.out.println("The cookie sent customer id is " + custId);

        return custId + " is the cookie value";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/cookie/object
    @GET
    @Path("/cookie/object")
    @Produces("text/html")
    public String getCookie(@CookieParam("customerId") Cookie custId) {
        System.out.println("The cookie sent customer id is " + custId.getValue());
        System.out.println("The cookie path is" + custId.getPath());
        System.out.println("The cookie domain is" + custId.getDomain());
        return custId.getValue() + " is the cookie value";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/beanparam
    // I used postman to send a request to this method (saved in Postman with name "Post Using FORM data")
    @POST
    @Path("/beanparam")
    public void createCar2(@BeanParam CarInput carInput) {

        System.out.println("Created a new car with model=" + carInput.getModel() + " and the year="+ carInput.getYear());

    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt5/cars/bmw/i3/red
    // here we use ColorEnum to have an enum instead of a String
    @GET
    @Path("/{model}/{color}")
    @Produces("text/html")
    public Object getPicture(@PathParam("maker") String maker,
                             @PathParam("model") String model,
                             @PathParam("color") ColorEnum color) {     // or you can use Color class from the same package


        System.out.println("Using enum as param way-> " + maker + " model " + model + " has car color is " + color.toString());
        return "IT worked :)";
    }

    public static enum ColorEnum
    {
        red,
        white,
        blue,
        black
    }
}
