package com.testehan.openliberty.chpt15.onetimepassword;

import jakarta.annotation.Priority;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.SecurityContext;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;

@OTPAuthenticated
@Priority(Priorities.AUTHENTICATION)
public class OneTimePasswordAuthenticator implements ContainerRequestFilter {

    /*
            The key of the map will be a username, while the value will be the secret password used by the user to
            create a one-time password.
     */
    protected Map<String, String> userSecretMap;

    public OneTimePasswordAuthenticator(Map<String, String> userSecretMap)
    {
        this.userSecretMap = userSecretMap;
    }


    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorization = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorization == null) {
            throw new NotAuthorizedException("One time password");
        }

        String[] split = authorization.split(" ");
        final String user = split[0];
        String otp = split[1];

        String secret = userSecretMap.get(user);
        if (secret == null) {
            throw new NotAuthorizedException("One time password");
        }
        String regen = OTP.generateToken(secret);
        System.out.println("generated secret is " + regen);
        if (!regen.equals(otp)) {
            throw new NotAuthorizedException("One time password");
        }

        /*
            After the user is authenticated, the filter() method creates a custom SecurityCon
            text implementation within an inner anonymous class. It then overrides the existing
            SecurityContext by calling ContainerRequestContext.setSecurityContext(). The
            SecurityContext.getUserPrincipal() is implemented to return a Principal initial‐
            ized with the username sent in the Authorization header. Other JAX-RS code can now
            inject this custom SecurityContext to find out who the user principal is.
        */

        final SecurityContext securityContext = requestContext.getSecurityContext();
        requestContext.setSecurityContext(new SecurityContext()
        {
            @Override
            public Principal getUserPrincipal()
            {
                return new Principal()
                {
                    @Override
                    public String getName()
                    {
                        return user;
                    }
                };
            }
            @Override
            public boolean isUserInRole(String role)
            {
                return false;
            }
            @Override
            public boolean isSecure()
            {
                return securityContext.isSecure();
            }
            @Override
            public String getAuthenticationScheme()
            {
                return "One Time Password";
            }
        });
    }
}
