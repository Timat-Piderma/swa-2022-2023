package com.stdt.aulewebrest.template.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import com.stdt.aulewebrest.template.exceptions.RESTWebApplicationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;

@Path("attrezzature")
public class AttrezzatureRes {
    
    @GET
    @Path("{idaula: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAttrezzature(
            @PathParam("idaula") int idaula,
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
            
            PreparedStatement ps = conn.prepareStatement("Select Attrezzatura.nome as Attrezzature from Fornito join Attrezzatura on Attrezzatura.ID = attrezzaturaID where aulaID = ?");
            ps.setInt(1, idaula);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                
                l.add(rs.getString("attrezzature"));
                
            }
            ps.close();
            
        } catch (NamingException ex) {
            Logger.getLogger(AttrezzatureRes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return Response.ok(l).build();
    }
    
}
