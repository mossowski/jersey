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
import com.misys.async_rest.dao.ProjectDao;

public class ProjectResourceTest extends JerseyTest {

    private String project1_id;
    private String project2_id;

    @Override
    protected Application configure() {

        // enable(TestProperties.LOG_TRAFFIC);
        // enable(TestProperties.DUMP_ENTITY);
        final PersonDao personDao = new PersonDao();
        final ProjectDao projectDao = new ProjectDao();
        return new ApplicationConfig(personDao, projectDao);
    }

    @Override
    protected void configureClient(ClientConfig clientConfig) {

        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();
        jsonProvider.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        clientConfig.register(jsonProvider);
    }

    protected Response addProject(String name, String... extras) {

        HashMap<String, Object> project = new HashMap<String, Object>();
        project.put("name", name);

        if (extras != null) {
            int count = 1;
            for (String a : extras) {
                project.put("extra" + count++, a);
            }
        }

        Entity<HashMap<String, Object>> projectEntity = Entity.entity(project,
                MediaType.APPLICATION_JSON_TYPE);
        return target("projects").request().post(projectEntity);
    }

    protected HashMap<String, Object> toHashMap(Response response) {
        return response.readEntity(new GenericType<HashMap<String, Object>>() {});
    }

    @Before
    public void setupProjects() {

        project1_id = (String) toHashMap(addProject("Project 1")).get("id");
        project2_id = (String) toHashMap(addProject("Project 2")).get("id");
    }

    @Test
    public void testAddProject() {

        Response response = addProject("Project 4");
        assertEquals(200, response.getStatus());

        HashMap<String, Object> responseProject = toHashMap(response);
        assertNotNull(responseProject.get("id"));
        assertEquals("Project 4", responseProject.get("name"));
    }

    @Test
    public void testGetProjectOne() {

        HashMap<String, Object> response = toHashMap(target("projects").path(project1_id).request()
                .get());
        assertNotNull(response);
    }

    @Test
    public void testGetProjectTwo() {

        HashMap<String, Object> response = toHashMap(target("projects").path(project2_id).request()
                .get());
        assertNotNull(response);
    }

    @Test
    public void testGetProjects() {

        Collection<HashMap<String, Object>> response = target("projects").request().get(
                new GenericType<Collection<HashMap<String, Object>>>() {});
        assertEquals(2, response.size());
    }

    @Test
    public void testAddExtraField() {

        Response response = addProject("name", "yolo");
        assertEquals(200, response.getStatus());
        HashMap<String, Object> project = toHashMap(response);
        assertNotNull(project.get("id"));
        // assertEquals(person.get("extra1"), "yolo");
    }

    @Test
    public void addProjectNoName() {

        Response response = addProject(null);
        assertEquals(400, response.getStatus());
        String message = response.readEntity(String.class);
        assertTrue(message.contains("name is required field"));
    }

}
