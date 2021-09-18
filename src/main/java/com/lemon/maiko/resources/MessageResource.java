package com.lemon.maiko.resources;

import com.codahale.metrics.annotation.Timed;
import com.lemon.maiko.core.services.MessageService;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/message")
@Produces(MediaType.TEXT_HTML)
public class MessageResource {


    private MessageService messageService;

    private static final String USER_API_ID_HEADER = "User-Api-Id";

    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    @GET
    @Timed
    public String getMessage(@HeaderParam(USER_API_ID_HEADER) String userApiId) {
        return this.messageService.getMessage(userApiId);
    }
}
