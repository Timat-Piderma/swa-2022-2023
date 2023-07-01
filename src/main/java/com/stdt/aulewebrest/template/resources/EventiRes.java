package com.stdt.aulewebrest.template.resources;

import com.stdt.aulewebrest.template.exceptions.RESTWebApplicationException;
import com.stdt.aulewebrest.template.model.Evento;
import com.stdt.aulewebrest.template.model.Tipologia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("eventi")
public class EventiRes {

    @GET
    @Path("{idevento: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public EventoRes getInfoEvento(
            @PathParam("idevento") int idevento,
            @Context UriInfo uriinfo,
            //iniettiamo elementi di contesto utili per la verifica d'accesso
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException {

        Evento evento = new Evento();

        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/progettoDB");
            Connection conn = ds.getConnection();

            PreparedStatement ps = conn.prepareStatement("Select * from Evento where ID = ?");
            ps.setInt(1, idevento);

            ResultSet rs = ps.executeQuery();

            rs.next();

            evento.setNome(rs.getString("nome"));
            evento.setData(LocalDate.parse(rs.getString("giorno")));
            evento.setOraInizio(LocalTime.parse(rs.getString("oraInizio")));
            evento.setOraFine(LocalTime.parse(rs.getString("oraFine")));
            evento.setDescrizione(rs.getString("descrizione"));
            evento.setTipologia(Tipologia.valueOf(rs.getString("tipologia")));

            System.out.println(evento.getNome());

        } catch (NamingException ex) {
            Logger.getLogger(EventiRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new EventoRes(evento);
    }

}
