package com.testehan.openliberty.chpt9;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.*;

import java.util.List;
import java.util.Locale;

@Path("chpt9/service")
public class Chpt9MyService {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt9/service/id/1
    @GET
    @Path("/id/{id}")
    @Produces({"application/xml", "application/json"})
    public String getCustomer(@PathParam("id") int id) {
        System.out.println("This works for both Accept values mentioned in the @Produces");

        return "application/xml or application/json";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt9/service
    @GET
    public Response getRequestedMediaTypeAndLanguage(@Context HttpHeaders headers) {
        // this is how you get the media type and language with the highest priority
        MediaType type = headers.getAcceptableMediaTypes().get(0);
        Locale language = headers.getAcceptableLanguages().get(0);
        Object responseObject = "Media type requested " + type + " with language requested " + language;

        Response.ResponseBuilder builder = Response.ok(responseObject, type);
        builder.language(language);
        return builder.build();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt9/service/variant
    /*
        The purpose of this method is to show how you could handle, in a single method, a variety combinations of
        media types x accepted languages x encodings...
     */
    @GET
    @Path("variant")
    public Response getVariantExample(@Context Request request) {
        Variant.VariantListBuilder vb = Variant.VariantListBuilder.newInstance();
        vb.mediaTypes(MediaType.APPLICATION_XML_TYPE, MediaType.APPLICATION_JSON_TYPE, MediaType.TEXT_PLAIN_TYPE)
            .languages(new Locale("en"), new Locale("es"))
            .encodings("deflate", "gzip","*").add();

        /*
            You interact with VariantListBuilder instances by calling the mediaTypes(), lan
            guages(), and encodings() methods. When you are done adding items, you invoke
            the build() method and it generates a Variant list containing all the possible combi‐
            nations of items you built it with.
         */

        List<Variant> variants = vb.build();

        // Pick the variant
        Variant v = request.selectVariant(variants);
        System.out.println(v.getMediaType() + "   " + v.getLanguage() + "   " + v.getEncoding());
        String entity =  " Random string that will be returned with the correcy media type, language and encoding ";
        Response.ResponseBuilder builder = Response.ok(entity);
        builder.type(v.getMediaType())
                .language(v.getLanguage());
//                .header("Content-Encoding", v.getEncoding());
//  for rome reason if this is uncommented it does not work...idk why did not spent too much on this..
        return builder.build();
    }
}
