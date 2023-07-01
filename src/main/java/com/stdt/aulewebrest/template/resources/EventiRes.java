package com.stdt.aulewebrest.template.resources;

import com.stdt.aulewebrest.template.exceptions.RESTWebApplicationException;
import com.stdt.aulewebrest.template.model.Evento;
import com.stdt.aulewebrest.template.model.Tipologia;
import java.net.URI;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

        } catch (NamingException ex) {
            Logger.getLogger(EventiRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new EventoRes(evento);
    }

    @GET
    @Path("{idaula: [0-9]+}/{giorno: [0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEventiSettimana(
            @PathParam("idaula") int idaula,
            @PathParam("giorno") String giorno,
            @Context UriInfo uriinfo,
            //iniettiamo elementi di contesto utili per la verifica d'accesso
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException {

        List<Evento> result = new ArrayList();

        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/progettoDB");
            Connection conn = ds.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM evento WHERE WEEK(evento.giorno)=WEEK(?) AND aulaID=?");

            ps.setString(1, giorno);
            ps.setInt(2, idaula);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Evento evento = new Evento();

                evento.setData(LocalDate.parse(rs.getString("giorno")));
                evento.setOraInizio(LocalTime.parse(rs.getString("oraInizio")));
                evento.setOraFine(LocalTime.parse(rs.getString("oraFine")));
                evento.setNome(rs.getString("nome"));
                evento.setDescrizione(rs.getString("descrizione"));
                evento.setTipologia(Tipologia.valueOf(rs.getString("tipologia")));

                result.add(evento);

            }

        } catch (NamingException ex) {
            Logger.getLogger(AttrezzatureRes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(result).build();
    }

    @GET
    @Path("{eventiAttuali}")
    @Produces("application/json")
    public Response getEventiAttuali(
            @Context UriInfo uriinfo,
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException {

        List<String> eventi = new ArrayList();

        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/progettoDB");
            Connection conn = ds.getConnection();

            PreparedStatement ps = conn.prepareStatement("SELECT * FROM evento WHERE evento.oraInizio >= CURRENT_TIMESTAMP AND evento.oraInizio <= CURRENT_TIMESTAMP + INTERVAL 3 HOUR AND evento.giorno= curdate()");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                eventi.add(rs.getString("nome"));

            }

        } catch (NamingException ex) {
            Logger.getLogger(AttrezzatureRes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(eventi).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addItem(
            @Context ContainerRequestContext req,
            @Context UriInfo uriinfo,
            @FormParam("giorno") String giorno,
            @FormParam("oraInizio") String oraInizio,
            @FormParam("oraFine") String oraFine,
            @FormParam("descrizione") String descrizione,
            @FormParam("nome") String nome,
            @FormParam("tipologia") String tipologia,
            @FormParam("idaula") String idaula,
            @FormParam("idresponsabile") String idresponsabile,
            @FormParam("idcorso") String idcorso
    ) throws SQLException, NamingException {

        InitialContext ctx;
        ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/progettoDB");
        Connection conn = ds.getConnection();

        PreparedStatement ps = conn.prepareStatement("INSERT INTO evento (giorno,oraInizio,oraFine,descrizione,nome,tipologia,aulaID,responsabileID,corsoID) VALUES(?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, giorno);
        ps.setString(2, oraInizio);
        ps.setString(3, oraFine);
        ps.setString(4, descrizione);
        ps.setString(5, nome);
        ps.setString(6, tipologia);
        ps.setString(7, idaula);
        ps.setString(8, idresponsabile);

        if (idcorso.isEmpty()) {
            ps.setString(9, null);
        } else {
            ps.setString(9, idcorso);
        }

        if (ps.executeUpdate() == 1) {

            ResultSet keys = ps.getGeneratedKeys();
            keys.next();

            URI uri = uriinfo.getBaseUriBuilder()
                    .build(keys.getInt(1));
            return Response.created(uri).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

    @POST
    @Path("{idevento: [0-9]+}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response UpdateItem(
            @Context ContainerRequestContext req,
            @Context UriInfo uriinfo,
            @FormParam("data") Date data,
            @FormParam("oraInizio") Time oraInizio,
            @FormParam("oraFine") Time oraFine,
            @FormParam("descrizione") String descrizione,
            @FormParam("nome") String nome,
            @FormParam("tipo") String tipo,
            @FormParam("idaula") String idaula,
            @FormParam("idresponsabile") String idresponsabile
    ) throws SQLException, NamingException {

        InitialContext ctx;
        ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/progettoDB");
        Connection conn = ds.getConnection();

        PreparedStatement ps = conn.prepareStatement("Select * from Evento where ID = ?");
        ps.setDate(1, (java.sql.Date) data);
        ps.setTime(2, oraInizio);
        ps.setTime(3, oraFine);
        ps.setString(4, descrizione);
        ps.setString(5, nome);
        ps.setString(6, tipo);
        ps.setString(7, idaula);
        ps.setString(8, idresponsabile);

        if (ps.executeUpdate() == 1) {

            ResultSet keys = ps.getGeneratedKeys();
            keys.next();

            URI uri = uriinfo.getBaseUriBuilder()
                    .path(EventoRes.class)
                    .path(EventoRes.class, "getItem")
                    .build(keys.getInt(1));
            return Response.created(uri).build();
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

}
