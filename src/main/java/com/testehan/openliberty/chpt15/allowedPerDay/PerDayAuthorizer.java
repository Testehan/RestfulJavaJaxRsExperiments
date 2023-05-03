package com.testehan.openliberty.chpt15.allowedPerDay;

import jakarta.annotation.Priority;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@AllowedPerDay(0)
@Priority(Priorities.AUTHORIZATION)
public class PerDayAuthorizer implements ContainerRequestFilter {

    @Context
    private ResourceInfo info;

    /*
        The filter instance remembers how many times in a day a particular user invoked a
        particular JAX-RS method. It stores this information in the count variable map. This
        map is keyed by a custom UserMethodKey class, which contains the username and JAXRS method that is being tracked.
     */
    private Map<UserMethodKey, Integer> count = new HashMap<>();
    private long today = System.currentTimeMillis();

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        SecurityContext sc = requestContext.getSecurityContext();
        if (sc == null) {
            throw new ForbiddenException();
        }
        Principal principal = sc.getUserPrincipal();
        if (principal == null) {
            throw new ForbiddenException();
        }
        String user = principal.getName();

        if (!authorized(user))
        {
            throw new ForbiddenException(" You have seen enough for today");
        }
    }

    /*
        The authorized() method is synchronized, as this filter may be concurrently accessed
        and we need to do this policy check atomically. It first checks to see if a day has elapsed.
    */
    protected synchronized boolean authorized(String user) {
        if (System.currentTimeMillis() > today + (24 * 60 * 60 * 1000)) {
            today = System.currentTimeMillis();
            count.clear();
        }

        UserMethodKey key = new UserMethodKey(user, info.getResourceMethod());
        Integer counter = count.get(key);
        if (counter == null)
        {
            counter = 0;
        }

        /*
            The method then extracts the AllowedPerDay annotation from the current JAX-RS
            method that is being invoked. This annotation will contain the number of times per day
            that a user is allowed to invoke the current JAX-RS method. If this value is greater than
            the current count for that user for that method, then we update the counter and return
            true. Otherwise, the policy check has failed and we return false
         */
        AllowedPerDay allowed = info.getResourceMethod().getAnnotation(AllowedPerDay.class);
        if (allowed.value() > counter)
        {
            count.put(key, counter + 1);
            return true;
        }
        return false;
    }


    private static class UserMethodKey
    {
        String username;
        Method method;
        public UserMethodKey(String username, Method method)
        {
            this.username = username;
            this.method = method;
        }
        @Override
        public boolean equals(Object o)
        {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            UserMethodKey that = (UserMethodKey) o;
            if (!method.equals(that.method)) return false;
            if (!username.equals(that.username)) return false;
            return true;
        }
        @Override
        public int hashCode()
        {
            int result = username.hashCode();
            result = 31 * result + method.hashCode();
            return result;
        }
    }


}
