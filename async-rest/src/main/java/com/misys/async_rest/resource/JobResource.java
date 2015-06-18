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
import com.misys.async_rest.dao.JobDao;
import com.misys.async_rest.model.Job;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/jobs")
public class JobResource {

    @Context
    JobDao dao;

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param response
     */
    @GET
    @ManagedAsync
    public void getJobs(@Suspended final AsyncResponse response) {

        ListenableFuture<Collection<Job>> jobsFuture = this.dao.getJobsAsync();
        Futures.addCallback(jobsFuture, new FutureCallback<Collection<Job>>() {
            @Override
            public void onSuccess(Collection<Job> jobs) {
                response.resume(jobs);
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
    public void getJob(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Job> jobFuture = this.dao.getJobAsync(id);
        Futures.addCallback(jobFuture, new FutureCallback<Job>() {
            @Override
            public void onSuccess(Job job) {
                response.resume(job);
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
     * @param job
     * @param response
     */
    @POST
    @ManagedAsync
    public void addJob(@Valid @NotNull Job job, @Suspended final AsyncResponse response) {

        ListenableFuture<Job> jobFuture = this.dao.addJobAsync(job);
        Futures.addCallback(jobFuture, new FutureCallback<Job>() {
            @Override
            public void onSuccess(Job addedJob) {
                response.resume(addedJob);
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
     * @param jobToUpdate
     * @param response
     */
    @PUT
    @ManagedAsync
    @Path("/{id}")
    public void updateJob(@PathParam("id") String id, @Valid @NotNull Job jobToUpdate, @Suspended final AsyncResponse response) {

        jobToUpdate.setId(id);
        ListenableFuture<Job> jobFuture = this.dao.updateJobAsync(jobToUpdate);
        Futures.addCallback(jobFuture, new FutureCallback<Job>() {
            @Override
            public void onSuccess(Job job) {
                response.resume(job);
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
    public void deleteJob(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Job> jobFuture = this.dao.deleteJobAsync(id);
        Futures.addCallback(jobFuture, new FutureCallback<Job>() {
            @Override
            public void onSuccess(Job job) {
                response.resume(job);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

}
