package com.misys.async_rest;

import org.glassfish.grizzly.http.server.CLStaticHttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.http.server.ServerConfiguration;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpContainer;
import org.glassfish.jersey.server.ResourceConfig;

import com.misys.async_rest.application.ApplicationConfig;
import com.misys.async_rest.dao.PersonDao;
import com.misys.async_rest.dao.ProjectDao;

import java.io.IOException;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.ext.RuntimeDelegate;

/**
 * Main class.
 *
 */
public class Main {

    public static final String WEB_ROOT = "/webroot/";
    public static final String APP_PATH = "/async-rest/";
    public static final int PORT = 8080;

    // ---------------------------------------------------------------------------------------------------

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * 
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {

        // Data Access Objects
        final PersonDao personDao = new PersonDao();
        final ProjectDao projectDao = new ProjectDao();
        // final Database db = new Database();

        final ResourceConfig rc = new ApplicationConfig(personDao, projectDao);
        //rc.registerClasses(MainResource.class);

        final HttpServer server = new HttpServer();
        final NetworkListener listener = new NetworkListener("grizzly", "localhost", PORT);

        server.addListener(listener);

        final ServerConfiguration config = server.getServerConfiguration();

        // add handler for serving static content
        config.addHttpHandler(new CLStaticHttpHandler(Main.class.getClassLoader(), WEB_ROOT),
                APP_PATH);

        // add handler for serving JAX-RS resources
        config.addHttpHandler(
                RuntimeDelegate.getInstance().createEndpoint(rc, GrizzlyHttpContainer.class),
                APP_PATH);

        try {
            server.start();
        } catch (Exception e) {
            throw new ProcessingException("Exception thrown when trying to start grizzly server", e);
        }

        return server;
    }

    // ---------------------------------------------------------------------------------------------------

    /*
     * public static ResourceConfig createResourceConfig() { return new
     * ResourceConfig().registerClasses(MainResource.class); }
     */

    // ---------------------------------------------------------------------------------------------------

    public static String getAppUri() {
        return String.format("http://localhost:%s%s", PORT, APP_PATH);
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * Main method.
     */
    public static void main(String[] args) throws IOException {

        final HttpServer server = startServer();
        System.out.println(String.format("Application started.\n" + "Access it at %s\n"
                + "Hit enter to stop it...", getAppUri()));
        System.in.read();
        server.shutdownNow();
    }

}
