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
	
	public PersonDao() {
		/*persons = new HashMap<String, Person>();
		Person p1 = new Person();
		p1.setId("1");
		p1.setName("Mietek");
		
		Person p2 = new Person();
		p2.setId("2");
		p2.setName("Wiesiek");
		
		persons.put(p1.getId(), p1);
		persons.put(p2.getId(), p2);*/
		
		persons = new ConcurrentHashMap<String, Person>();
		service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
		
	}
	
	public Collection<Person> getPersons() {
		return persons.values();
	}
	
	public Person getPerson(String id) {
		return persons.get(id);
	}
	
	public Person addPerson(Person person) {
		person.setId(UUID.randomUUID().toString());
		persons.put(person.getId(), person);
		
		return person;
	}
	
	public ListenableFuture<Person> addPersonAsync(final Person person) {
		ListenableFuture<Person> future = 
				service.submit(new Callable<Person>() {
					public Person call() throws Exception {
						return addPerson(person);
					}
				});
		return future;
	}
	
}
