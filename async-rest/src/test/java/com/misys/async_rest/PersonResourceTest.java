package com.misys.async_rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.misys.async_rest.application.ApplicationConfig;
import com.misys.async_rest.dao.PersonDao;

public class PersonResourceTest extends JerseyTest {

	private String person1_id;
	private String person2_id;
	
	@Override
	protected Application configure() {
		//enable(TestProperties.LOG_TRAFFIC);
		//enable(TestProperties.DUMP_ENTITY);
		final PersonDao dao = new PersonDao();
		return new ApplicationConfig(dao);
	}
	
	@Override
	protected void configureClient(ClientConfig clientConfig) {
		JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
		jsonProvider.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
		clientConfig.register(jsonProvider);
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
		//System.out.println("PERSON N : " + person);
		Entity<HashMap<String, Object>> personEntity = Entity.entity(person, MediaType.APPLICATION_JSON_TYPE);
		return target("persons").request().post(personEntity);
	}
	
	protected HashMap<String, Object> toHashMap(Response response) {
		return response.readEntity(new GenericType<HashMap<String, Object>>() {});
	}
	
	@Before
	public void setupPersons() {
		person1_id = (String)toHashMap(addPerson("Mietek")).get("id");
		person2_id = (String)toHashMap(addPerson("Wiesiek")).get("id");
	}
	
	@Test
	public void testAddPerson() {
		Response response = addPerson("Bolek");
		assertEquals(200, response.getStatus());
		
		HashMap<String, Object> responsePerson = toHashMap(response);
		assertNotNull(responsePerson.get("id"));
		assertEquals("Bolek", responsePerson.get("name"));	
	}
	
	@Test
	public void testGetPerson() {
		HashMap<String, Object> response = toHashMap(target("persons").path(person1_id).request().get());
		assertNotNull(response);
	}
	
	@Test
	public void testGetPersons() {
		Collection <HashMap<String, Object>> response = target("persons").request().get(new GenericType<Collection<HashMap<String, Object>>>(){});
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
		//HashMap<String, Object> person = response.readEntity(new GenericType<HashMap<String, Object>>() {});
		HashMap<String, Object> person = toHashMap(response);
		assertNotNull(person.get("id"));
		System.out.println(" PERSON : " + person);
		//assertEquals(person.get("extra1"), "yolo");
	}
	
	@Test
	public void addPersonNoName() {
		Response response = addPerson(null);
		assertEquals(400, response.getStatus());
		String message = response.readEntity(String.class);
		assertTrue(message.contains("name is required field"));
	}
	
}
