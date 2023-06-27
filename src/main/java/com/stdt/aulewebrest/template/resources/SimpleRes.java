package com.stdt.aulewebrest.template.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import com.stdt.aulewebrest.template.exceptions.RESTWebApplicationException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author didattica
 */
@Path("simple")
public class SimpleRes {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
            @Context UriInfo uriinfo,
            @QueryParam("p") String parametro) throws RESTWebApplicationException {

        List<String> l = new ArrayList();
        l.add("cidassdasadasdao1");
        l.add("ciao2");

        return Response.ok(l).build();
    }

    @GET
    @Path("pog")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPOG(
            @Context UriInfo uriinfo,
            @QueryParam("p") String parametro) throws RESTWebApplicationException, SQLException, ClassNotFoundException {

        List<String> l = new ArrayList();

        String sqlSelectAllPersons = "SELECT * FROM evento";

        InitialContext ctx;
        try {
            ctx = new InitialContext();
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/progettoDB");
            Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(sqlSelectAllPersons);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                l.add("nome: " + rs.getString("nome"));

            }

        } catch (NamingException ex) {
            Logger.getLogger(SimpleRes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return Response.ok(l).build();
    }

    @GET
    @Path("{item: [0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItem(@PathParam("item") int itemID) {
        if (itemID < 1000) {
            /* non presente */
            return Response.status(404).entity("item not found").build();
        } else {
            return Response.ok(itemID).build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addMe(
            @Context UriInfo uriinfo, String payload) {
        return Response.created(
                uriinfo.getAbsolutePathBuilder()
                        .path(this.getClass(), "getItem").build(1000))
                .build();
    }

}
