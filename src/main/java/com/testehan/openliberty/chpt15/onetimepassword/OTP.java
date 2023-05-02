package com.testehan.openliberty.chpt15.onetimepassword;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class OTP {

    public static String generateToken(String secret)
    {
        long minutes = System.currentTimeMillis() / 1000 / 60;
        System.out.println("Minutes concatenated " + minutes);
        String concat = secret + minutes;
        MessageDigest digest;
        try
        {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new IllegalArgumentException(e);
        }
        byte[] hash = digest.digest(concat.getBytes(Charset.forName("UTF-8")));
        return Base64.getEncoder().encodeToString(hash); //
    }

}
