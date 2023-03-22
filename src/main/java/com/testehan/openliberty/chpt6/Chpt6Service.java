package com.testehan.openliberty.chpt6;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.StreamingOutput;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import java.io.*;
import java.util.Scanner;

@Path("chpt6")
public class Chpt6Service {

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/streamingoutput
    @GET
    @Path("streamingoutput")
    @Produces("text/plain")
    public StreamingOutput getStreamingOutput() {
        return new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                output.write("hello world from getStreamingOutput".getBytes());
            }
        };
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/inputstream
    @PUT
    @Path("inputstream")
    public void putInputStream(InputStream is) throws IOException {
        byte[] bytes = readFromStream(is);
        String input = new String(bytes);
        System.out.println("Using inputstream" +input);
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/reader
    @PUT
    @Path("reader")
    public void putInputStream(Reader reader) throws IOException {
        LineNumberReader lineReader = new LineNumberReader(reader);
        System.out.println("Using reader");
        String line;
        do {
            line = lineReader.readLine();
            if (line != null) {
                System.out.println(line);
            }
        } while (line != null);
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/returninputstream
    @GET
    @Path("returninputstream")
    @Produces("text/plain")
    public InputStream getReturninputstream() throws IOException {
        System.out.println("Trying to read a fileeee");
//        FileInputStream is = new FileInputStream("/data/chpt6.txt");
        //  InputStream inputStream = Reader.class.getResourceAsStream("/data/chpt6.txt");
        // it does not find the file for some reason to read it...idk why it seems like the file location
        // is okay compared to OpenLibertyExperiments project where i had a similar scenario..I wasted enough time
        // trying to figure out why it does not find the file...and so..whenever I will face this in a real life situation
        // i will dig deeper to understand why it is not working

        FileInputStream is = new FileInputStream("abc.txt");
        // this however works, but only if the enpoint from below which creates
        // the file is called before
        return is;
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/returnfile
    @GET
    @Path("returnfile")
    @Produces("text/plain")
    public File getFile() throws IOException {
        System.out.println("Trying to return a file");
        File file = new File("abc.txt");

        FileWriter myWriter = new FileWriter(file);
        myWriter.write("Files in Java might be tricky, but it is fun enough!");
        myWriter.close();

        return file;
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/postfile
    // https://stackoverflow.com/questions/39037049/how-to-upload-a-file-and-json-data-in-postman
    @POST
    @Path("postfile")
    public void postFile(File file) throws IOException {
        System.out.println("Printing file content");
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            System.out.println(data);
        }
        myReader.close();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/bytes
    @GET
    @Path("bytes")
    @Produces("text/plain")
    public byte[] getBytes() {
        return "hello world".getBytes();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/bytes
    @POST
    @Path("bytes")
    @Consumes("text/plain")
    public void postBytes(byte[] bytes) {
        System.out.println(new String(bytes));
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/string
    @GET
    @Path("string")
    @Produces("application/xml")
    public String getString() {
        return "<customer><name>Bill Burke</name></customer>";
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/string
    @POST
    @Path("string")
    @Consumes("text/plain")
    public void postString(String str) {
        System.out.println(str);
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/multivaluedmap
    /*
        we saw how you can use the @FormParam annotation to inject individual form parameters from the re‐
        quest. You can also inject a MultivaluedMap<String, String> that represents all the
        form data sent with the request
     */
    @POST
    @Path("multivaluedmap")
    @Consumes("application/x-www-form-urlencoded")
    @Produces("application/x-www-form-urlencoded")
    public MultivaluedMap<String,String> postMultivaluedMap(MultivaluedMap<String, String> form) {
        return form;
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/dummy/jaxb
    @GET
    @Path("dummy/jaxb")
    @Produces("text/plain")
    public String getDummyXMLCall() throws JAXBException {
        Customer customer = createDummyCustomer();
        JAXBContext ctx = JAXBContext.newInstance(Customer.class);
        StringWriter writer = new StringWriter();
        ctx.createMarshaller().marshal(customer, writer);
        String custString = writer.toString();
        customer = (Customer)ctx.createUnmarshaller()
                .unmarshal(new StringReader(custString));

        System.out.println(customer);

        return customer.toString();
    }

    private static Customer createDummyCustomer() {
        Customer customer = new Customer();
        customer.setId(42);
        customer.setFullname("Bill Burke");
        customer.setAddress(new Address("Tokio","knichiwa","tokio","3242345"));
        return customer;
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/postcustomer
    @POST
    @Path("postcustomer")
    @Consumes("application/xml")
    public void createCustomer(Customer cust) {
        System.out.println(cust.toString());
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/postreturncustomer
    @POST
    @Path("postreturncustomer")
    @Consumes("application/xml")
    public Customer createReturnCustomer(Customer cust) {
        System.out.println("Posted customer " + cust.toString());
        return cust;
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/postreturncustomer
    @GET
    @Path("postreturncustomer")
    @Consumes("application/xml")
    public Customer getCustomer() {
        Customer cus = createDummyCustomer();
        System.out.println("Customer that is returned " + cus);
        return createDummyCustomer();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/getcustomer
    @GET
    @Path("getcustomer")
    @Produces("application/xml")
    public Customer getcustomer() {
        Customer customer = createDummyCustomer();
        return customer;
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt6/getcustomerjson
    // this is pretty impressive..although my Customer class has only JAXB / xml related annotations
    // the translation from such an object to json formart is straight forward..no other changes are needed..
    @GET
    @Path("getcustomerjson")
    @Produces("application/json")
    public Customer getcustomerjson() {
        Customer customer = createDummyCustomer();
        return customer;
    }

    private byte[] readFromStream(InputStream stream)  throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1000];
        int wasRead = 0;
        do {
            wasRead = stream.read(buffer);
            if (wasRead > 0) {
                baos.write(buffer, 0, wasRead);
            }
        } while (wasRead > -1);
        return baos.toByteArray();
    }
}
