package com.lemon.maiko.resources;

import com.codahale.metrics.annotation.Timed;
import com.lemon.maiko.api.res.Message;
import com.lemon.maiko.core.services.MessageService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/message")
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {


    private MessageService messageService;

    public MessageResource(MessageService messageService) {
        this.messageService = messageService;
    }

    @GET
    @Timed
    public Message getMessage(@QueryParam("name") Optional<String> name) {
        return this.messageService.getMessage();
    }
}
