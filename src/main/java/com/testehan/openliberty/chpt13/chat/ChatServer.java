package com.testehan.openliberty.chpt13.chat;

import jakarta.ws.rs.*;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

@Path("chpt13/chat")
public class ChatServer {

    class Message {
        String id;
        String message;
        Message next;
    }

    protected Message first;
    protected Message last;

    protected int maxMessages = 10;
    protected LinkedHashMap<String, Message> messages = new LinkedHashMap<String, Message>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Message> eldest) {
            boolean remove = size() > maxMessages;
            if (remove) first = eldest.getValue().next;
            return remove;
        }
    };

    protected AtomicLong counter = new AtomicLong(0);

    LinkedList<AsyncResponse> listeners = new LinkedList<>();

    ExecutorService writer = Executors.newSingleThreadExecutor();

    // http://192.168.1.129:9080/RestfulJavaJaxRsExperiments/services/chpt13/chat
    @POST
    @Consumes("text/plain")
    public void post(@Context UriInfo uriInfo, final String text) {
        final UriBuilder base = uriInfo.getBaseUriBuilder();
        writer.submit(new Runnable() {
            @Override
            public void run() {
                synchronized (messages) {
                    Message message = new Message();
                    message.id = Long.toString(counter.incrementAndGet());
                    message.message = text;

                    if (messages.size() == 0) {
                        first = message;
                    } else {
                        last.next = message;
                    }
                    messages.put(message.id, message);
                    last = message;
                    for (AsyncResponse async : listeners) {
                        try {
                            send(base, async, message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    listeners.clear();
                }
            }
        });
    }

    @GET
    public void receive(@Context UriInfo uriInfo,
                        @QueryParam("current") String id,
                        @Suspended AsyncResponse async) {
        final UriBuilder base = uriInfo.getBaseUriBuilder();
        Message message = null;
        synchronized (messages) {
            Message current = messages.get(id);
            if (current == null) {
                message = first;
            }
            else {
                message = current.next;
            }
            if (message == null) {
                queue(async);
            }
        }
        // do this outside of synchronized block to reduce lock hold time
        if (message != null) {
            send(base, async, message);
        }
    }

    protected void queue(AsyncResponse async) {
        listeners.add(async);
    }

    protected void send(UriBuilder base, AsyncResponse async, Message message) {
        URI nextUri = base.clone().path(ChatServer.class)
                                  .queryParam("current", message.id)
                                  .build();
        Link next = Link.fromUri(nextUri).rel("next").build();
        Response response = Response.ok(message.message, MediaType.TEXT_PLAIN_TYPE)
                                    .links(next)
                                    .build();
        async.resume(response);
    }

}
