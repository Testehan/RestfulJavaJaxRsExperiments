package com.testehan.openliberty.chpt15.onetimepassword;

import jakarta.ws.rs.NameBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
    The first custom security feature we’ll write is one-time password (OTP) authentication.
    The client will use a credential that changes once per minute. This credential will be a
    hash that we generate by combining a static password with the current time in minutes.
    The client will send this generated one-time password in the Authorization header.
    For example:
        GET /customers HTTP/1.1
        Authorization: <username> <generated_password>

    We will enforce OTP authentication only on JAX-RS methods annotated with the
    @OTPAuthenticated annotation
*/

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface OTPAuthenticated
{
}
