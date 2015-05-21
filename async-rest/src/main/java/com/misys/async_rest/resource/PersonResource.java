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
import com.misys.async_rest.dao.PersonDao;
import com.misys.async_rest.model.Person;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/persons")
public class PersonResource {

    @Context
    PersonDao dao;

    // ---------------------------------------------------------------------------------------------------

    @GET
    @ManagedAsync
    public void getPersons(@Suspended final AsyncResponse response) {

        ListenableFuture<Collection<Person>> personsFuture = dao.getPersonsAsync();
        Futures.addCallback(personsFuture, new FutureCallback<Collection<Person>>() {
            @Override
            public void onSuccess(Collection<Person> persons) {
                response.resume(persons);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------------

    @GET
    @ManagedAsync
    @Path("/{id}")
    public void getPerson(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Person> personFuture = dao.getPersonAsync(id);
        Futures.addCallback(personFuture, new FutureCallback<Person>() {
            @Override
            public void onSuccess(Person person) {
                response.resume(person);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------------

    @POST
    @ManagedAsync
    public void addPerson(@Valid @NotNull Person person, @Suspended final AsyncResponse response) {

        ListenableFuture<Person> personFuture = dao.addPersonAsync(person);
        Futures.addCallback(personFuture, new FutureCallback<Person>() {
            @Override
            public void onSuccess(Person addedPerson) {
                response.resume(addedPerson);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }
    
    // ---------------------------------------------------------------------------------------------------

    @PUT
    @ManagedAsync
    @Path("/{id}")
    public void updatePerson(@PathParam("id") String id, @Valid @NotNull Person personToUpdate, @Suspended final AsyncResponse response) {

        personToUpdate.setId(id);
        ListenableFuture<Person> personFuture = dao.updatePersonAsync(personToUpdate);
        Futures.addCallback(personFuture, new FutureCallback<Person>() {
            @Override
            public void onSuccess(Person person) {
                response.resume(person);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }
    
    // ---------------------------------------------------------------------------------------------------

    @DELETE
    @ManagedAsync
    @Path("/{id}")
    public void deletePerson(@PathParam("id") String id, @Suspended final AsyncResponse response) {

        ListenableFuture<Person> personFuture = dao.deletePersonAsync(id);
        Futures.addCallback(personFuture, new FutureCallback<Person>() {
            @Override
            public void onSuccess(Person person) {
                response.resume(person);
            }

            @Override
            public void onFailure(Throwable thrown) {
                response.resume(thrown);
            }
        });
    }

}
