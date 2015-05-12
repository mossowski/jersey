package com.misys.async_rest.resource;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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


@Path("/projects")
public class ProjectResource {

	@Context ProjectDao dao;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Project> getProjects() {
		return dao.getProjects();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Project getProject(@PathParam("id") String id) {
		return dao.getProject(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	public void addProject(Project project, @Suspended final AsyncResponse response) {
		ListenableFuture<Project> projectFuture = dao.addProjectAsync(project);
		Futures.addCallback(projectFuture, new FutureCallback<Project>() {
			public void onSuccess(Project addedProject) {
				response.resume(addedProject);
			}
			public void onFailure(Throwable thrown) {
				response.resume(thrown);
			}
			
		});
	}
	
}