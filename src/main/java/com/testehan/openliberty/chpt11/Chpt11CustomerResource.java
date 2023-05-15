package com.testehan.openliberty.chpt11;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.time.Instant;
import java.util.Date;

@Path("chpt11")
public class Chpt11CustomerResource {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt11/cachedstring
    // if you made a request then the method will be called. however if i open a new chrome tab, then it will give the results
    // from the cache. If i press refresh however, chrome seems to ignore the header. Same behaviour in safari as well.

    // " the Expires header was deprecated in favor of the Cache-Control header"
    @GET
    @Path("cachedstring")
    @Produces("text/plain")
    public Response getCachedString() {

        String s = "This is cached so it will return this date if the server is running until 15 oct 2023 ==> " + Instant.now();
        Response.ResponseBuilder builder = Response.ok(s);

        builder.expires(Date.from(Instant.ofEpochMilli(1697353810000l)));   // Sun Oct 15 2023 10:10:10
        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt11/cachecontrolheader
    @GET
    @Path("cachecontrolheader")
    @Produces("text/plain")
    public Response getCacheControlResponseHeader() {
        String s = "This is cached so it will return this date if the server is running until 15 oct 2023 ==> " + Instant.now();

        CacheControl cc = new CacheControl();
        cc.setMaxAge(300);
        cc.setPrivate(true);
        cc.setNoStore(true);
        Response.ResponseBuilder builder = Response.ok(s);
        builder.cacheControl(cc);
        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt11/entitytag
    /*
            An instance of javax.ws.rs.core.Request is injected
             into the method using the @Context annotation. We then find a String instance
            and create a current ETag value for it from the hash code of the object (this isn’t the best
            way to create the EntityTag, but for simplicity’s sake, let’s keep it that way). We then
            call Request.evaluatePreconditions(), passing in the up-to-date tag. If the tags
            match, we reset the client’s cache expiration by sending a new Cache-Control header
            and return. If the tags don’t match, we build a Response with the new, current version
            of the ETag and String. ...obviously instead of String you can have Customer Order etc
     */
    @GET
    @Path("entitytag")
    @Produces("text/plain")
    public Response getEntityTag(@Context Request request) {
        String responseString = "This is cached so it will return this date if the server is " +
                "running until 15 oct 2023 ==> ";// + Instant.now();

        EntityTag tag = new EntityTag(Integer.toString(responseString.hashCode()));
        CacheControl cc = new CacheControl();
        cc.setMaxAge(1000);
        Response.ResponseBuilder builder = request.evaluatePreconditions(tag);
        if (builder != null) {
            builder.cacheControl(cc);
            return builder.build();
        }
        // Preconditions not met!
        builder = Response.ok(responseString);
        builder.cacheControl(cc);
        builder.tag(tag);
        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt11/updateConcurrently
    // i mean the method would look different in real life, with the request body / object appearing in the list of
    // parameters..I simplified to just use a string...also you need to provide corect values in the request headers
    // Header               Value
    // If-Match             3141271342554322343200
    // If-Unmodified-Since  Fri, 17 Mar 2020 07:59:12 GMT
    // --> this will trigger the update to not go through (412, “Precondition Failed.”) because the hash and the timestamp are not equal
    // Header               Value
    // If-Match             1516993645
    // If-Unmodified-Since  Wed, 15 Mar 2023 00:00:00 GMT
    // --> this will trigger the update to go through because the resource was not modified by anyone else in the meantime
    @PUT
    @Path("updateConcurrently")
    @Consumes("application/xml")
    public Response updateConcurrently(@Context Request request) {
        String responseString = "initial string"; // that will be updated
        EntityTag tag = new EntityTag(Integer.toString(responseString.hashCode()));
        Date timestamp = Date.from(Instant.ofEpochMilli(1678831200000l)); // get the timestamp in some application specific way     1678831200000
        Response.ResponseBuilder builder = request.evaluatePreconditions(timestamp, tag);
        if (builder != null) {
            // Preconditions not met!
            System.out.println(responseString);
            return builder.build();
        }

        // if we reach this part, it means that the value was modified and we need to update string
        responseString = "updated string";
        System.out.println(responseString);

        builder = Response.noContent();
        return builder.build();
    }
}
