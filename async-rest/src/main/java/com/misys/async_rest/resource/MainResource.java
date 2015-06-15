package com.misys.async_rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/")
public class MainResource {

    @GET
    public void print() {

        System.out.println("DZIALA");
    }
}
