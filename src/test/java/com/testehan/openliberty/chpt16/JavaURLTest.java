package com.testehan.openliberty.chpt16;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class JavaURLTest {

    @Test
    public void callingGetWithHttpConnectionWithJavaNetURL() throws IOException {
        URL url = new URL("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/string");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");

        // https://stackoverflow.com/questions/24520528/what-method-call-in-httpsurlconnection-actually-sends-the-http-request
        // this is the method that makes the call to the server..
        if (connection.getResponseCode() != 200) {
            throw new RuntimeException("Operation failed: " + connection.getResponseCode());
        }

        System.out.println("Content-Type: " + connection.getContentType());
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = reader.readLine();
        while (line != null) {
            System.out.println(line);
            line = reader.readLine();
        }
        connection.disconnect();
    }

    @Test
    public void callingPostWithHttpConnectionWithJavaNetUrl() throws IOException {
        URL url = new URL("http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/string");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // We need to call HttpURLConnection.setDoOutput(true). This  allows us to write a body
        // for the request. By default, HttpURLConnection will automatically follow redirects
        connection.setDoOutput(true);
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/plain");

        OutputStream os = connection.getOutputStream();
        os.write("<customer id='333'/>".getBytes());
        os.flush();

        if (connection.getResponseCode() != HttpURLConnection.HTTP_NO_CONTENT) {
            throw new RuntimeException("Maybe the message was not recieved..");
        }

        connection.disconnect();
    }
}
