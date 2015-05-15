package com.misys.async_rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Collection;
import java.util.HashMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import com.misys.async_rest.application.PersonApplication;
import com.misys.async_rest.dao.PersonDao;
import com.misys.async_rest.model.Person;

public class PersonResourceTest extends JerseyTest {

	private String person1_id;
	private String person2_id;
	
	protected Application configure() {
		//enable(TestProperties.LOG_TRAFFIC);
		//enable(TestProperties.DUMP_ENTITY);
		final PersonDao dao = new PersonDao();
		return new PersonApplication(dao);
	}
	
	protected Response addPerson(String name, String...extras) {
		
		HashMap<String, Object> person = new HashMap<String, Object>();
		person.put("name", name);
		if (extras != null) {
			int count = 1;
			for (String a : extras) {
				System.out.println(count + " " + person);
				person.put("extra" + count++, a);
				
			}
		}
		Entity<HashMap<String, Object>> personEntity = Entity.entity(person, MediaType.APPLICATION_JSON_TYPE);
		return target("persons").request().post(personEntity);
	}
	
	protected HashMap<String, Object> toHashMap(Response response) {
		return response.readEntity(new GenericType<HashMap<String, Object>>() {});
	}
	
	@Before
	public void setupPersons() {
		person1_id = addPerson("Mietek").readEntity(Person.class).getId();
		person2_id = addPerson("Wiesiek").readEntity(Person.class).getId();
	}
	
	@Test
	public void testAddPerson() {
		Response response = addPerson("Bolek");
		assertEquals(200, response.getStatus());
		
		Person responsePerson = response.readEntity(Person.class);
		assertNotNull(responsePerson.getId());
		assertEquals("Bolek", responsePerson.getName());	
	}
	
	@Test
    	public void testGetPerson() {
		Person response = target("persons").path(person1_id).request().get(Person.class);
        	assertNotNull(response);
    	}
	
	@Test
    	public void testGetPersons() {
        	Collection <Person> response = target("persons").request().get(new GenericType<Collection<Person>>(){});
        	assertEquals(2, response.size());
    	}
	
	/*@Test
	public void testDao() {
		Person response1 = target("persons").path("1").request().get(Person.class);
		Person response2 = target("persons").path("1").request().get(Person.class);
		assertEquals(response1.getName(), response2.getName());
	}*/
	
	@Test
	public void testAddExtraField() {
		Response response = addPerson("name", "yolo");
		assertEquals(200, response.getStatus());
		
		HashMap<String, Object> person = response.readEntity(new GenericType<HashMap<String, Object>>() {});
		//HashMap<String, Object> person = toHashMap(response);
		assertNotNull(person.get("id"));
		assertEquals(person.get("extra1"), "yolo");
	}
	
}
