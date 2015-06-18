package com.misys.async_rest.resource;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ManagedAsync;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.misys.async_rest.dao.BuildDao;
import com.misys.async_rest.model.Build;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/builds")
public class BuildResource {

    @Context
    BuildDao dao;

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param response
     */
    @GET
    @ManagedAsync
    public void getBuilds(@Suspended final AsyncResponse response) {

        ListenableFuture<Collection<Build>> buildsFuture = this.dao.getBuildsAsync();
        Futures.addCallback(buildsFuture, new FutureCallback<Collection<Build>>() {
            @Override
            public void onSuccess(Collection<Build> builds) {
                response.resume(builds);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @param response
     */
    @GET
    @ManagedAsync
    @Path("/{id}")
    public void getBuild(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Build> buildFuture = this.dao.getBuildAsync(id);
        Futures.addCallback(buildFuture, new FutureCallback<Build>() {
            @Override
            public void onSuccess(Build build) {
                response.resume(build);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param build
     * @param response
     */
    @POST
    @ManagedAsync
    public void addBuild(@Valid @NotNull Build build, @Suspended final AsyncResponse response) {

        ListenableFuture<Build> buildFuture = this.dao.addBuildAsync(build);
        Futures.addCallback(buildFuture, new FutureCallback<Build>() {
            @Override
            public void onSuccess(Build addedBuild) {
                response.resume(addedBuild);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @param buildToUpdate
     * @param response
     */
    @PUT
    @ManagedAsync
    @Path("/{id}")
    public void updateBuild(@PathParam("id") String id, @Valid @NotNull Build buildToUpdate, @Suspended final AsyncResponse response) {

        buildToUpdate.setId(id);
        ListenableFuture<Build> buildFuture = this.dao.updateBuildAsync(buildToUpdate);
        Futures.addCallback(buildFuture, new FutureCallback<Build>() {
            @Override
            public void onSuccess(Build build) {
                response.resume(build);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @param response
     */
    @DELETE
    @ManagedAsync
    @Path("/{id}")
    public void deleteBuild(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Build> buildFuture = this.dao.deleteBuildAsync(id);
        Futures.addCallback(buildFuture, new FutureCallback<Build>() {
            @Override
            public void onSuccess(Build build) {
                response.resume(build);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

}
