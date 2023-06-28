/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.stdt.aulewebrest.template.resources;

import com.stdt.aulewebrest.template.exceptions.RESTWebApplicationException;
import com.stdt.aulewebrest.template.model.Tipologia;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

/**
 *
 * @author aless
 */
@Path("eventi")
public class EventiRes {
  @GET
    @Path("{idevento: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getInfoEvento(
            @PathParam("idevento") int idevento,
            @Context UriInfo uriinfo,
            //iniettiamo elementi di contesto utili per la verifica d'accesso
            @Context SecurityContext sec,
            @Context ContainerRequestContext req)
            throws RESTWebApplicationException, SQLException, ClassNotFoundException {
        
         List<String> l = new ArrayList();
        
        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/progettoDB");
            Connection conn = ds.getConnection();
            
            PreparedStatement ps = conn.prepareStatement("Select * from Evento where ID = ?");
            ps.setInt(1, idevento);
            
            ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                l.add(rs.getDate("giorno").toString());
                l.add(rs.getTime("oraInizio").toString());
                l.add(rs.getTime("oraFine").toString());
                l.add(rs.getString("nome"));
                l.add(rs.getString("descrizione"));
                
                
                }
            
        } catch (NamingException ex) {
            Logger.getLogger(AttrezzatureRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return Response.ok(l).build();
    }
    
}
