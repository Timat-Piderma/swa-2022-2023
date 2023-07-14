package com.stdt.aulewebrest.template.resources;

import com.stdt.aulewebrest.template.exceptions.RESTWebApplicationException;
import com.stdt.aulewebrest.template.model.Aula;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

public class AulaRes {

    private final Aula aula;

    AulaRes(Aula aula) {
        this.aula = aula;
    }

    @GET
    @Produces("application/json")
    public Response getItem() {

        try {
            return Response.ok(aula).build();
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

    }

}
