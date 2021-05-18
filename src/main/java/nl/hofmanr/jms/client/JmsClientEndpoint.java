package nl.hofmanr.jms.client;

import nl.hofmanr.jms.client.domain.JmsMessage;
import nl.hofmanr.jms.client.domain.JmsPayload;
import nl.hofmanr.jms.client.domain.JmsQueue;
import nl.hofmanr.jms.client.service.QueueService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

@Path("/queues")
@Produces(MediaType.APPLICATION_JSON)
public class JmsClientEndpoint {
    private static final Logger LOGGER = Logger.getLogger(JmsClientEndpoint.class.getName());

    @Inject
    QueueService queueService;

    @GET
    public Response getQueues() {
        List<JmsQueue> queues = queueService.getQueues();
        if (queues.size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok(queues).build();
    }

    @GET
    @Path("/{queue}/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response count(@PathParam("queue") String queueName) {
        int count = queueService.countMessages(queueName);
        return Response.ok(count).build();
    }

    @GET
    @Path("/{queue}/clear")
    public Response clear(@PathParam("queue") String queueName) {
        queueService.deleteMessages(queueName);
        return Response.ok().build();
    }

    @GET
    @Path("/{queue}/messages")
    public Response getMessages(@PathParam("queue") String queueName) {
        List<JmsMessage> messages = queueService.getMessages(queueName);
        if (messages.size() == 0) {
            return Response.noContent().build();
        }
        return Response.ok(messages).build();
    }

    @GET
    @Path("/{queue}/messages/{messageID}")
    public Response getMessage(@PathParam("queue") String queueName, @PathParam("messageID") String messageID) {
        JmsPayload payload= queueService.getMessage(queueName, messageID);
        if (payload == null) {
            return Response.noContent().build();
        }
        return Response.ok(payload).build();
    }

    @POST
    @Path("/{queue}/messages")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendMessage(@PathParam("queue") String queueName, String message, @Context UriInfo uriInfo) {
        String messageID = queueService.addMessage(queueName, message);
        if (messageID == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        URI createdURI = uriInfo.getBaseUriBuilder().path("queues/" + queueName + "/messages/" + messageID).build();
        return Response.created(createdURI).build();
    }

    @DELETE
    @Path("/{queue}/messages/{messageID}")
    public Response deletMessage(@PathParam("queue") String queueName, @PathParam("messageID") String messageID) {
        queueService.deleteMessage(queueName, messageID);
        return Response.noContent().build();
    }

}
