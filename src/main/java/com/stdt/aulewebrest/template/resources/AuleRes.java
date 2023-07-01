package com.stdt.aulewebrest.template.resources;

import com.stdt.aulewebrest.template.exceptions.RESTWebApplicationException;
import com.stdt.aulewebrest.template.model.Aula;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

@Path("aule")
public class AuleRes {

    @GET
    @Path("{idaula: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public AulaRes getInfoAula(
            @PathParam("idaula") int idaula,
            @Context UriInfo uriinfo,
            //iniettiamo elementi di contesto utili per la verifica d'accesso
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException {

        Aula aula = new Aula();

        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/progettoDB");
            Connection conn = ds.getConnection();

            PreparedStatement ps = conn.prepareStatement("Select * from Aula where ID = ?");
            ps.setInt(1, idaula);

            ResultSet rs = ps.executeQuery();

            rs.next();

            aula.setNome(rs.getString("nome"));
            aula.setCapienza(rs.getInt("capienza"));
            aula.setEmailResponsabile(rs.getString("emailResponsabile"));
            aula.setNumeroPreseRete(rs.getInt("numeroPreseRete"));
            aula.setNote(rs.getString("note"));
            aula.setNumeroPreseElettriche(rs.getInt("numeroPreseElettriche"));

        } catch (NamingException ex) {
            Logger.getLogger(AuleRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new AulaRes(aula);
    }
}
