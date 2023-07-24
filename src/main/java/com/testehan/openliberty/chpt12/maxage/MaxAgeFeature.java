package com.testehan.openliberty.chpt12.maxage;

import jakarta.ws.rs.container.DynamicFeature;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.FeatureContext;
import jakarta.ws.rs.ext.Provider;

/*
    The MaxAgeFeature.configure() method is invoked for every deployed JAX-RS re‐
    source method. The configure() method first looks for the @MaxAge annotation on the
    ResourceInfo’s method. If it exists, it constructs an instance of the CacheControlFil
    ter, passing in the value of the @MaxAge annotation. It then registers this created filter
    with the FeatureContext parameter. This filter is now bound to the JAX-RS resource
    method represented by the ResourceInfo parameter. We’ve just created a JAX-RS
    extension!
*/

@Provider
public class MaxAgeFeature implements DynamicFeature {

    public void configure(ResourceInfo ri, FeatureContext ctx) {
        MaxAge max = ri.getResourceMethod().getAnnotation(MaxAge.class);
        if (max == null) return;
        CacheControlFilterMaxAge filter = new CacheControlFilterMaxAge(max.value());
        ctx.register(filter);
    }
}
