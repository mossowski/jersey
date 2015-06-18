package com.misys.async_rest.dao;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;





import static com.mongodb.client.model.Filters.*;

import java.util.concurrent.Executors;

import org.bson.Document;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.misys.async_rest.Main;
import com.misys.async_rest.model.Person;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class PersonDao {

    private ListeningExecutorService service;

    // ---------------------------------------------------------------------------------------------------

    public PersonDao() {

        this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public Collection<Person> getPersons() {
        
        Map<String, Person> persons = new ConcurrentHashMap<String, Person>();
        
        for (Document cursor : Main.database.persons.find()) {
            Person person = new Person();
            person.setId(cursor.get("id").toString());
            person.setName(cursor.get("name").toString());
            persons.put(person.getId(), person);
        }
        
        return persons.values();
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public ListenableFuture<Collection<Person>> getPersonsAsync() {

        ListenableFuture<Collection<Person>> future = this.service
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

        Document document = Main.database.persons.find(eq("id", id)).first();
        Person person = new Person();
        
        person.setId(document.get("id").toString());
        person.setName(document.get("name").toString());
        
        System.out.println("\n------------- GET PERSON WITH ID ----------------");
        System.out.println(" id   : " + document.get("id"));
        System.out.println(" name : " + document.get("name"));
        System.out.println("-------------------------------------------------");       
        
        return person;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Person> getPersonAsync(final String id) {

        ListenableFuture<Person> future = this.service.submit(new Callable<Person>() {
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
        
        Document document = new Document();
        document.put("id", person.getId());
        document.put("name", person.getName());
        
        Main.database.persons.insertOne(document);

        return person;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param person
     * @return
     */
    public ListenableFuture<Person> addPersonAsync(final Person person) {

        ListenableFuture<Person> future = this.service.submit(new Callable<Person>() {
            @Override
            public Person call() throws Exception {
                return addPerson(person);
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
    public Person updatePerson(Person person) {

        String personId = person.getId();
        String personName = person.getName();
        
        Map<String, Object> changes = new ConcurrentHashMap<String, Object>();
        changes.put("id", personId);
        changes.put("name", personName);
        
        UpdateResult result = Main.database.persons.updateOne(eq("id", person.getId()), new Document("$set", new Document(changes)));
        
        System.out.println("\n------------- UPDATE PERSON WITH ID -------------");
        System.out.println(" id     : " + personId);
        System.out.println(" name   : " + personName);
        System.out.println(" result :" + result);
        System.out.println("-------------------------------------------------");       
        
        return person;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param person
     * @return
     */
    public ListenableFuture<Person> updatePersonAsync(final Person person) {

        ListenableFuture<Person> future = this.service.submit(new Callable<Person>() {
            @Override
            public Person call() throws Exception {
                return updatePerson(person);
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
    public Person deletePerson(String id) {

        Person personToDelete = getPerson(id);
        DeleteResult result = Main.database.persons.deleteOne(eq("id", id));
        
        System.out.println("\n------------- DELETE PERSON WITH ID -------------");
        System.out.println(" id       : " + id);
        System.out.println(" name     : " + personToDelete.getName());
        System.out.println(" result   : " + result);
        System.out.println("-------------------------------------------------"); 
        
        return personToDelete;
        
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Person> deletePersonAsync(final String id) {

        ListenableFuture<Person> future = this.service.submit(new Callable<Person>() {
            @Override
            public Person call() throws Exception {
                return deletePerson(id);
            }
        });

        return future;
    }

}
