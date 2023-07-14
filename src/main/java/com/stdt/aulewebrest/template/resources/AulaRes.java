package com.stdt.aulewebrest.template.resources;

import com.stdt.aulewebrest.framework.security.Logged;
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

        /*
        Provato con il serializer...
            
        AulaSerializer serializer = new AulaSerializer();
        ObjectMapperContextResolver objMapper = new ObjectMapperContextResolver();
        JsonFactory jsonFactory = objMapper.getContext(Aula.class).getFactory();
        StringWriter jsonObjectWriter = new StringWriter();

        try ( JsonGenerator jsonGenerator = jsonFactory.createGenerator(jsonObjectWriter)) {
            serializer.serialize(aula, jsonGenerator, null);
            return Response.ok(jsonObjectWriter.toString()).build();
        }*/
        try {
            return Response.ok(aula).build();
        } catch (Exception e) {
            throw new RESTWebApplicationException(e);
        }

    }

}
