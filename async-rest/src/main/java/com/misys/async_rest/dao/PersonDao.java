package com.misys.async_rest.dao;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.misys.async_rest.model.Person;

public class PersonDao {

    private Map<String, Person> persons;
    private ListeningExecutorService service;

    // ---------------------------------------------------------------------------------------------------

    public PersonDao() {

        persons = new ConcurrentHashMap<String, Person>();
        service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public Collection<Person> getPersons() {
        return persons.values();
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public ListenableFuture<Collection<Person>> getPersonsAsync() {

        ListenableFuture<Collection<Person>> future = service
                .submit(new Callable<Collection<Person>>() {
                    @Override
                    public Collection<Person> call() throws Exception {
                        return getPersons();
                    }
                });

        return future;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public Person getPerson(String id) {
        return persons.get(id);
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Person> getPersonAsync(final String id) {

        ListenableFuture<Person> future = service.submit(new Callable<Person>() {
            @Override
            public Person call() throws Exception {
                return getPerson(id);
            }
        });

        return future;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param person
     * @return
     */
    public Person addPerson(Person person) {

        person.setId(UUID.randomUUID().toString());
        persons.put(person.getId(), person);

        return person;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param person
     * @return
     */
    public ListenableFuture<Person> addPersonAsync(final Person person) {

        ListenableFuture<Person> future = service.submit(new Callable<Person>() {
            @Override
            public Person call() throws Exception {
                return addPerson(person);
            }
        });

        return future;
    }

}
