package nl.hofmanr.jms.client;

import io.swagger.annotations.*;
import nl.hofmanr.jms.client.domain.JmsMessage;
import nl.hofmanr.jms.client.domain.JmsPayload;
import nl.hofmanr.jms.client.domain.JmsQueue;
import nl.hofmanr.jms.client.exception.DataAccessException;
import nl.hofmanr.jms.client.exception.ServiceException;
import nl.hofmanr.jms.client.service.QueueService;

import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.logging.Logger;

@Path("/queues")
@Api("Queue")
@Produces(MediaType.APPLICATION_JSON)
public class JmsClientEndpoint {
    private static final Logger LOGGER = Logger.getLogger(JmsClientEndpoint.class.getName());

    @Inject
    QueueService queueService;

    @ApiOperation(value = "Returns all available queues", response = JmsQueue.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Queues found"),
            @ApiResponse(code = 204, message = "No queues available on server"),
            @ApiResponse(code = 500, message = "Server error")
    })
    @GET
    public Response getQueues() {
        try {
            List<JmsQueue> queues = queueService.getQueues();
            if (queues.size() == 0) {
                return Response.noContent().build();
            }
            return Response.ok(queues).build();
        } catch (ServiceException se) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getExceptionMessage(se)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Returns number of entries on a queue", response = Integer.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Queues found"),
            @ApiResponse(code = 404, message = "Queue unknown"),
            @ApiResponse(code = 500, message = "Server error")
    })
    @GET
    @Path("/{queue}/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response count(@PathParam("queue") String queueName) {
        try {
            int count = queueService.countMessages(queueName);
            return Response.ok(count).build();
        } catch (ServiceException se) {
            String errorMessage = getExceptionMessage(se);
            if (errorMessage.startsWith("No queue with name"))
                return Response.status(Response.Status.NOT_FOUND).entity(getExceptionMessage(se)).build(); // 404
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getExceptionMessage(se)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Delete al messages from a given queue")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Messages deleted"),
            @ApiResponse(code = 404, message = "Queue unknown"),
            @ApiResponse(code = 500, message = "Server error")
    })
    @DELETE
    @Path("/{queue}/clear")
    public Response clear(@PathParam("queue") String queueName) {
        try {
            queueService.deleteMessages(queueName);
            return Response.ok().build();
        } catch (ServiceException se) {
            String errorMessage = getExceptionMessage(se);
            if (errorMessage.startsWith("No queue with name"))
                return Response.status(Response.Status.NOT_FOUND).entity(getExceptionMessage(se)).build(); // 404
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getExceptionMessage(se)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Returns all messages for a given queue", response = JmsMessage.class, responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Messages found"),
            @ApiResponse(code = 204, message = "No messages found"),
            @ApiResponse(code = 404, message = "Queue unknown"),
            @ApiResponse(code = 500, message = "Server error")
    })
    @GET
    @Path("/{queue}/messages")
    public Response getMessages(@PathParam("queue") String queueName) {
        try {
            List<JmsMessage> messages = queueService.getMessages(queueName);
            if (messages.size() == 0) {
                return Response.noContent().build();
            }
            return Response.ok(messages).build();
        } catch (ServiceException se) {
            String errorMessage = getExceptionMessage(se);
            if (errorMessage.startsWith("No queue with name"))
                return Response.status(Response.Status.NOT_FOUND).entity(getExceptionMessage(se)).build(); // 404
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getExceptionMessage(se)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Returns the payload for a given queue-entry", response = JmsPayload.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Payload found"),
            @ApiResponse(code = 204, message = "No payload found"),
            @ApiResponse(code = 404, message = "Queue unknown"),
            @ApiResponse(code = 500, message = "Server error")
    })
    @GET
    @Path("/{queue}/messages/{messageID}")
    public Response getMessage(@PathParam("queue") String queueName, @PathParam("messageID") String messageID) {
        try {
            JmsPayload payload = queueService.getMessage(queueName, messageID);
            if (payload == null) {
                return Response.noContent().build();
            }
            return Response.ok(payload).build();
        } catch (ServiceException se) {
            String errorMessage = getExceptionMessage(se);
            if (errorMessage.startsWith("No queue with name"))
                return Response.status(Response.Status.NOT_FOUND).entity(getExceptionMessage(se)).build(); // 404
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getExceptionMessage(se)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @ApiOperation(value = "Submit a message to a queue",
            response=String.class,
            consumes="text/plain"
    )
    @ApiResponses({
            @ApiResponse(code = 201, message = "The message is submitted"),
            @ApiResponse(code = 404, message = "Queue unknown"),
            @ApiResponse(code = 500, message = "Server error")
    })
    @POST
    @Path("/{queue}/messages")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response sendMessage(@PathParam("queue") String queueName,
                                @ApiParam(value="This parameter encapsulates the payload", required = true) String payload,
                                @Context UriInfo uriInfo) {
        try {
            String messageID = queueService.addMessage(queueName, payload);
            if (messageID == null) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
            URI createdURI = uriInfo.getBaseUriBuilder().path("queues/" + queueName + "/messages/" + messageID).build();
            return Response.created(createdURI).build();
        } catch (ServiceException se) {
            String errorMessage = getExceptionMessage(se);
            if (errorMessage.startsWith("No queue with name"))
                return Response.status(Response.Status.NOT_FOUND).entity(getExceptionMessage(se)).build(); // 404
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getExceptionMessage(se)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @ApiOperation("Deletes a message from a given queue")
    @ApiResponses({
            @ApiResponse(code = 204, message = "The message has been deleted"),
            @ApiResponse(code = 404, message = "Queue unknown"),
            @ApiResponse(code = 500, message = "Server error")
    })
    @DELETE
    @Path("/{queue}/messages/{messageID}")
    public Response deletMessage(@PathParam("queue") String queueName, @PathParam("messageID") String messageID) {
        try {
            queueService.deleteMessage(queueName, messageID);
            return Response.noContent().build();
        } catch (ServiceException se) {
            String errorMessage = getExceptionMessage(se);
            if (errorMessage.startsWith("No queue with name"))
                return Response.status(Response.Status.NOT_FOUND).entity(getExceptionMessage(se)).build(); // 404
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getExceptionMessage(se)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    private String getExceptionMessage(ServiceException se) {
        if (se.getCause() instanceof DataAccessException) {
            DataAccessException de = (DataAccessException) se.getCause();
            if (de.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException ce = (ConstraintViolationException) de.getCause();
                if (ce.getConstraintViolations() != null && !ce.getConstraintViolations().isEmpty())
                    return ce.getConstraintViolations().iterator().next().getMessage();
            }
            return de.getMessage();
        }
        return se.getMessage();
    }

}
