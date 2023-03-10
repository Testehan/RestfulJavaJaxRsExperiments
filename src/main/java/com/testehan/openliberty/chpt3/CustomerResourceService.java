package com.testehan.openliberty.chpt3;

import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class CustomerResourceService implements CustomerResource{

    private Map<Integer, Customer> customerDB = new ConcurrentHashMap<>();
    private AtomicInteger idCounterr = new AtomicInteger();

    // you can override the metadata defined in your interfaces by reapplying annotations within your implementation class.
    // i've done this for demonstration purposes only..
//    @POST
//    @Consumes("application/xml;charset=utf-8")
    public Response createCustomer(InputStream is) {
        Customer customer = readCustomer(is);
        customer.setId(idCounterr.incrementAndGet());
        customerDB.put(customer.getId(), customer);
        System.out.println("Created customerrrr " + customer.getId());
        return Response.created(URI.create("/customers/" + customer.getId())).build();
    }

    public StreamingOutput getCustomer(@PathParam("id") int id) {
        final Customer customer = customerDB.get(id);
        if (customer == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                PrintStream writer = new PrintStream(outputStream);
                outputCustomer(writer, customer);
            }
        };
    }

    @Override
    public StreamingOutput getCustomerByFirstAndLastName(@PathParam("firstname") String first,
                                                         @PathParam("lastname") String last) {
        return getCustomer(1);  // I mean i just want to test the @Path our, not to code an actual implementation
    }

    @Override
    public StreamingOutput getAllCustomers() {
        List<Customer> allCustomers = customerDB.entrySet().stream().map(entry->entry.getValue()).collect(Collectors.toList());
        return new StreamingOutput() {
            public void write(OutputStream outputStream) throws IOException, WebApplicationException {
                PrintStream writer = new PrintStream(outputStream);
                writer.println("<customers>");
                outputAllCustomers(writer, allCustomers);
                writer.println("</customers>");
            }
        };

    }

    public void updateCustomer(@PathParam("id") int id, InputStream is) {
        Customer update = readCustomer(is);
        Customer current = customerDB.get(id);
        if (current == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
        current.setStreet(update.getStreet());
        current.setState(update.getState());
        current.setZip(update.getZip());
        current.setCountry(update.getCountry());
        current.setCity(update.getCity());
    }

    private void outputAllCustomers(final PrintStream writer, final List<Customer> allCustomers) throws IOException {
        for (Customer customer: allCustomers) {
            outputCustomer(writer,customer);
        }
    }

    private void outputCustomer(final PrintStream writer, final Customer cust) throws IOException {
        writer.println("<customer id=\"" + cust.getId() + "\">");
        writer.println(" <first-name>" + cust.getFirstName() + "</first-name>");
        writer.println(" <last-name>" + cust.getLastName() + "</last-name>");
        writer.println(" <street>" + cust.getStreet() + "</street>");
        writer.println(" <city>" + cust.getCity() + "</city>");
        writer.println(" <state>" + cust.getState() + "</state>");
        writer.println(" <zip>" + cust.getZip() + "</zip>");
        writer.println(" <country>" + cust.getCountry() + "</country>");
        writer.println("</customer>");
    }

    private Customer readCustomer(final InputStream is) {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(is);
            Element root = doc.getDocumentElement();

            Customer cust = new Customer();
            if (root.getAttribute("id") != null
                    && !root.getAttribute("id").trim().equals("")) {
                cust.setId(Integer.valueOf(root.getAttribute("id")));
            }
            NodeList nodes = root.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                if (element.getTagName().equals("first-name")) {
                    cust.setFirstName(element.getTextContent());
                } else if (element.getTagName().equals("last-name")) {
                    cust.setLastName(element.getTextContent());
                } else if (element.getTagName().equals("street")) {
                    cust.setStreet(element.getTextContent());
                } else if (element.getTagName().equals("city")) {
                    cust.setCity(element.getTextContent());
                } else if (element.getTagName().equals("state")) {
                    cust.setState(element.getTextContent());
                } else if (element.getTagName().equals("zip")) {
                    cust.setZip(element.getTextContent());
                } else if (element.getTagName().equals("country")) {
                    cust.setCountry(element.getTextContent());
                }
            }
            return cust;
        }
        catch (Exception e) {
            System.out.println(e);
            throw new WebApplicationException(e, Response.Status.BAD_REQUEST);
        }
    }
}
