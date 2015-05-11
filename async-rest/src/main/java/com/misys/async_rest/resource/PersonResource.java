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
import com.misys.async_rest.dao.PersonDao;
import com.misys.async_rest.model.Person;


@Path("/persons")
public class PersonResource {

	@Context PersonDao dao;
	//PersonDao dao = new PersonDao();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Person> getPersons() {
		return dao.getPersons();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{id}")
	public Person getPerson(@PathParam("id") String id) {
		return dao.getPerson(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ManagedAsync
	public void addPerson(Person person, @Suspended final AsyncResponse response) {
		//response.resume(dao.addPerson(person));
		ListenableFuture<Person> personFuture = dao.addPersonAsync(person);
		Futures.addCallback(personFuture, new FutureCallback<Person>() {
			public void onSuccess(Person addedPerson) {
				response.resume(addedPerson);
			}
			public void onFailure(Throwable thrown) {
				response.resume(thrown);
			}
			
		});
	}
	
}
