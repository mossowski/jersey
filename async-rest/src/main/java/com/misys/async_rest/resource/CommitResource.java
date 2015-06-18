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
import com.misys.async_rest.dao.CommitDao;
import com.misys.async_rest.model.Commit;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/commits")
public class CommitResource {

    @Context
    CommitDao dao;

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param response
     */
    @GET
    @ManagedAsync
    public void getCommits(@Suspended final AsyncResponse response) {

        ListenableFuture<Collection<Commit>> commitsFuture = this.dao.getCommitsAsync();
        Futures.addCallback(commitsFuture, new FutureCallback<Collection<Commit>>() {
            @Override
            public void onSuccess(Collection<Commit> commits) {
                response.resume(commits);
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
    public void getCommit(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Commit> commitFuture = this.dao.getCommitAsync(id);
        Futures.addCallback(commitFuture, new FutureCallback<Commit>() {
            @Override
            public void onSuccess(Commit commit) {
                response.resume(commit);
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
     * @param commit
     * @param response
     */
    @POST
    @ManagedAsync
    public void addCommit(@Valid @NotNull Commit commit, @Suspended final AsyncResponse response) {

        ListenableFuture<Commit> commitFuture = this.dao.addCommitAsync(commit);
        Futures.addCallback(commitFuture, new FutureCallback<Commit>() {
            @Override
            public void onSuccess(Commit addedCommit) {
                response.resume(addedCommit);
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
     * @param commitToUpdate
     * @param response
     */
    @PUT
    @ManagedAsync
    @Path("/{id}")
    public void updateCommit(@PathParam("id") String id, @Valid @NotNull Commit commitToUpdate, @Suspended final AsyncResponse response) {

        commitToUpdate.setId(id);
        ListenableFuture<Commit> commitFuture = this.dao.updateCommitAsync(commitToUpdate);
        Futures.addCallback(commitFuture, new FutureCallback<Commit>() {
            @Override
            public void onSuccess(Commit commit) {
                response.resume(commit);
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
    public void deleteCommit(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Commit> commitFuture = this.dao.deleteCommitAsync(id);
        Futures.addCallback(commitFuture, new FutureCallback<Commit>() {
            @Override
            public void onSuccess(Commit commit) {
                response.resume(commit);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

}
