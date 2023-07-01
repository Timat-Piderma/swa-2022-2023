package com.stdt.aulewebrest.template.resources;

import com.stdt.aulewebrest.template.exceptions.RESTWebApplicationException;
import com.stdt.aulewebrest.template.model.Evento;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public class EventoRes {

    private final Evento evento;

    EventoRes(Evento evento) {
        this.evento = evento;
    }

    @GET
    @Produces("application/json")
    public Response getItem() {

        try {
            return Response.ok(evento).build();
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

    }

}
