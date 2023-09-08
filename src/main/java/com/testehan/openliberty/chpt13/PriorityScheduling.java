package com.testehan.openliberty.chpt13;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Path("chpt13/scheduling")
public class PriorityScheduling {
    Executor executor;
    public PriorityScheduling() {
        executor = Executors.newSingleThreadExecutor();
    }

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/scheduling/year_to_date_report
    // in postman there are 2 requests called "PriorityScheduling" and "PriorityScheduling2" ..if you send them both
    // you will see that once the executor starts working on the generation of the report for the first request, the second one
    // is waiting until the first generation is taken care of.
    @POST
    @Path("year_to_date_report")
    @Consumes("text/plain")
    public void ytdReport(String productId, @Suspended AsyncResponse response) {
        executor.execute( new Runnable() {
            public void run() {
                String report = null;
                try {
                    report = generateYTDReportFor(productId);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                response.resume(report);
            }
        });
    }
    protected String generateYTDReportFor(String productId) throws InterruptedException {
        System.out.println("Will generate report for " + productId);
        Thread.sleep(10000);
        return "report for product " + productId + "  "  + Instant.now(Clock.systemUTC());
    }
}
