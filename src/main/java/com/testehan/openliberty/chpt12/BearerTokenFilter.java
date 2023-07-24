package com.testehan.openliberty.chpt12;

import com.testehan.openliberty.chpt12.tokenAuthAnnotation.TokenAuthenticated;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)    // because we want this filter to be executed before other filters; the smaller the number the faster it will run
@TokenAuthenticated
public class BearerTokenFilter implements ContainerRequestFilter {

    // if you send the request called "Authorization header needed" from Postman, that contains the Authorization
    // header with the "abc" value, then it will work...if you send a request with no Authorization header it will fail,
    // and this is why i commented out the code
    public void filter(ContainerRequestContext ctx) throws IOException {
//        String authHeader = ctx.getHeaderString(HttpHeaders.AUTHORIZATION);
//        if (authHeader == null) throw new NotAuthorizedException("Bearer");
//        String token = parseToken(authHeader);
//        if (verifyToken(token) == false) {
//            throw new NotAuthorizedException("Bearer error=\"invalid_token\"");
//        }
    }
    private String parseToken(String header) {
        return header;
    }
    private boolean verifyToken(String token) {
        if (token.equalsIgnoreCase("abc")){
            return true;
        }
        return false;
    }

}
