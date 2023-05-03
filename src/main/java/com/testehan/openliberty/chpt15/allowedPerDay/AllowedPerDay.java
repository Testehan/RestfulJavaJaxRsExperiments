package com.testehan.openliberty.chpt15.allowedPerDay;

import jakarta.ws.rs.NameBinding;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
    Allowed-per-Day Access Policy
    The next custom security feature we’ll implement is an allowed-per-day access policy.
    The idea is that for a certain JAX-RS method, we’ll specify how many times each user
    is allowed to execute that method per day. We will do this by applying the @Allowed
    PerDay annotation to a JAX-RS method

*/


@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@NameBinding
public @interface AllowedPerDay
{
    int value();
}
