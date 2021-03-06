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
import com.misys.async_rest.dao.ProjectDao;
import com.misys.async_rest.model.Project;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/projects")
public class ProjectResource {

    @Context
    ProjectDao dao;

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param response
     */
    @GET
    @ManagedAsync
    public void getProjects(@Suspended final AsyncResponse response) {

        ListenableFuture<Collection<Project>> projectsFuture = this.dao.getProjectsAsync();
        Futures.addCallback(projectsFuture, new FutureCallback<Collection<Project>>() {
            @Override
            public void onSuccess(Collection<Project> projects) {
                response.resume(projects);
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
    public void getProject(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Project> projectFuture = this.dao.getProjectAsync(id);
        Futures.addCallback(projectFuture, new FutureCallback<Project>() {
            @Override
            public void onSuccess(Project project) {
                response.resume(project);
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
     * @param project
     * @param response
     */
    @POST
    @ManagedAsync
    public void addProject(@Valid @NotNull Project project, @Suspended final AsyncResponse response) {

        ListenableFuture<Project> projectFuture = this.dao.addProjectAsync(project);
        Futures.addCallback(projectFuture, new FutureCallback<Project>() {
            @Override
            public void onSuccess(Project addedProject) {
                response.resume(addedProject);
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
     * @param projectToUpdate
     * @param response
     */
    @PUT
    @ManagedAsync
    @Path("/{id}")
    public void updateProject(@PathParam("id") String id, @Valid @NotNull Project projectToUpdate, @Suspended final AsyncResponse response) {

        projectToUpdate.setId(id);
        ListenableFuture<Project> projectFuture = this.dao.updateProjectAsync(projectToUpdate);
        Futures.addCallback(projectFuture, new FutureCallback<Project>() {
            @Override
            public void onSuccess(Project project) {
                response.resume(project);
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
    public void deleteProject(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Project> projectFuture = this.dao.deleteProjectAsync(id);
        Futures.addCallback(projectFuture, new FutureCallback<Project>() {
            @Override
            public void onSuccess(Project project) {
                response.resume(project);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

}
