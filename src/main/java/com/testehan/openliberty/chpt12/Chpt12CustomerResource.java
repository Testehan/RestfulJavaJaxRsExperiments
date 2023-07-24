package com.testehan.openliberty.chpt12;


import com.testehan.openliberty.chpt12.maxage.MaxAge;
import com.testehan.openliberty.chpt12.tokenAuthAnnotation.TokenAuthenticated;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.Date;

@Path("chpt12")
public class Chpt12CustomerResource {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt12/maxage
    // if you made a request then the method will be called. however if i open a new chrome tab, then it will give the results
    // from the cache. If i press refresh however, chrome seems to ignore the header. Same behaviour in safari as well.

    // " the Expires header was deprecated in favor of the Cache-Control header"
    @GET
    @Path("maxage")
    @Produces("text/plain")
    @MaxAge(100000) // because of this you will see max-age=100000 in the response header (this is implemented in my code it is not a standard JAXRS annotation)
    public Response getCachedString() {

        String s = "This is cached so it will return this date if the server is running until 15 oct 2023 ==> " + Instant.now();
        Response.ResponseBuilder builder = Response.ok(s);

        builder.expires(Date.from(Instant.ofEpochMilli(1697353810000l)));   // Sun Oct 15 2023 10:10:10
        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt12/authorization
    // this methods needs the Authorization header to contain the value "abc" in order to work
    @GET
    @Path("authorization")
    @Produces("text/plain")
    @TokenAuthenticated // because of this you will authorization from BearerTokenFilter will be performed
    public Response getAuthorization() {

        String s = "You will see this only if authorized :) ";
        Response.ResponseBuilder builder = Response.ok(s);

        return builder.build();
    }


}
