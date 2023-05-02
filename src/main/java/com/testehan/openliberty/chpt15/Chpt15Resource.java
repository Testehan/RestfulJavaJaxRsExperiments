package com.testehan.openliberty.chpt15;

import com.testehan.openliberty.chpt15.allowedPerDay.AllowedPerDay;
import com.testehan.openliberty.chpt15.onetimepassword.OTPAuthenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("chpt15")
public class Chpt15Resource {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt15/securedget
    @GET
    @Path("securedget")
    @Produces("text/plain")
    public Response getValueConfigured() throws InterruptedException {
        Response.ResponseBuilder builder = Response.ok("You should see this only if you are authenticated and authorized ");
        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt15/securexml
    // you will be asked to login..but since the realm is not defined, the login will not work
    // also the user will need to be mapped to the mentioned role in order for this to work..
    @GET
    @Path("securexml")
    @Produces("application/xml")
    @RolesAllowed("XML-USERS")
    public String getXmlCustomers() {
        return "xml";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt15/securejson
    @GET
    @Path("securejson")
    @Produces("application/json")
    @RolesAllowed("JSON-USERS")
    public String getJsonCustomers() {
        return "json";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt15/logusers
    // Let’s say we want to have a security
    // log of all access to a customer database by users who are not administrators.
    @GET
    @Path("logusers")
    public String getCustomers(@Context SecurityContext sec) {
        if (sec.isSecure() && !sec.isUserInRole("ADMIN")) {
            System.out.println(sec.getUserPrincipal() + " accessed customer database.");
        }
        return "bla bla";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt15/onetimepassword
    // In Postman there is a request that sends an Authorization header with format "username generated_password"
    // and you need to set obvious the second part, which depends on what is the current minute since 1970.
    // for this there is a message in the server console, from where you can take generated_password
    @GET
    @Path("onetimepassword")
    @OTPAuthenticated
    public String getResourceProtectedByOneTimePassword() {
        return "This is...Victoria's secret... :)";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt15/otpandmaxtwice
    // In Postman there is a request that sends an Authorization header with format "username generated_password"
    // and you need to set obvious the second part, which depends on what is the current minute since 1970.
    // for this there is a message in the server console, from where you can take generated_password
    // you can only call this method 2 times per day because of @AllowedPerDay(2)
    @GET
    @Path("otpandmaxtwice")
    @OTPAuthenticated
    @AllowedPerDay(2)
    public String getResourceProtectedByOneTimePasswordOnlyAllowedToBeCalledTwicePerDay() {
        return "This is...Victoria's secret... :) ...accesed only max 2 times";
    }
}
