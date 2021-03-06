package com.misys.async_rest.dao;

import static com.mongodb.client.model.Filters.eq;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import org.bson.Document;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.misys.async_rest.Main;
import com.misys.async_rest.model.Project;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class ProjectDao {

	private ListeningExecutorService service;

    // ---------------------------------------------------------------------------------------------------

    public ProjectDao() {

        this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public Collection<Project> getProjects() {
        
        Map<String, Project> projects = new ConcurrentHashMap<String, Project>();
        
        for (Document cursor : Main.database.getProjects().find()) {
            Project project = new Project();
            project.setId(cursor.get("id").toString());
            project.setName(cursor.get("name").toString());
            projects.put(project.getId(), project);
        }
        
        return projects.values();
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public ListenableFuture<Collection<Project>> getProjectsAsync() {

        ListenableFuture<Collection<Project>> future = this.service
                .submit(new Callable<Collection<Project>>() {
                    @Override
                    public Collection<Project> call() throws Exception {
                        return getProjects();
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
    public Project getProject(String id) {

        Document document = Main.database.getProjects().find(eq("id", id)).first();
        Project project = new Project();
        
        project.setId(document.get("id").toString());
        project.setName(document.get("name").toString());
        
        System.out.println("\n------------- GET PROJECT WITH ID ----------------");
        System.out.println(" id   : " + document.get("id"));
        System.out.println(" name : " + document.get("name"));
        System.out.println("-------------------------------------------------");       
        
        return project;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Project> getProjectAsync(final String id) {

        ListenableFuture<Project> future = this.service.submit(new Callable<Project>() {
            @Override
            public Project call() throws Exception {
                return getProject(id);
            }
        });

        return future;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param project
     * @return
     */
    public Project addProject(Project project) {

        project.setId(UUID.randomUUID().toString());
        
        Document document = new Document();
        document.put("id", project.getId());
        document.put("name", project.getName());
        
        Main.database.getProjects().insertOne(document);

        return project;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param project
     * @return
     */
    public ListenableFuture<Project> addProjectAsync(final Project project) {

        ListenableFuture<Project> future = this.service.submit(new Callable<Project>() {
            @Override
            public Project call() throws Exception {
                return addProject(project);
            }
        });

        return future;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param project
     * @return
     */
    public Project updateProject(Project project) {

        String projectId = project.getId();
        String projectName = project.getName();
        
        Map<String, Object> changes = new ConcurrentHashMap<String, Object>();
        changes.put("id", projectId);
        changes.put("name", projectName);
        
        UpdateResult result = Main.database.getProjects().updateOne(eq("id", project.getId()), new Document("$set", new Document(changes)));
        
        System.out.println("\n------------- UPDATE PROJECT WITH ID -------------");
        System.out.println(" id     : " + projectId);
        System.out.println(" name   : " + projectName);
        System.out.println(" result :" + result);
        System.out.println("-------------------------------------------------");       
        
        return project;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param project
     * @return
     */
    public ListenableFuture<Project> updateProjectAsync(final Project project) {

        ListenableFuture<Project> future = this.service.submit(new Callable<Project>() {
            @Override
            public Project call() throws Exception {
                return updateProject(project);
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
    public Project deleteProject(String id) {

        Project projectToDelete = getProject(id);
        DeleteResult result = Main.database.getProjects().deleteOne(eq("id", id));
        
        System.out.println("\n------------- DELETE PROJECT WITH ID -------------");
        System.out.println(" id       : " + id);
        System.out.println(" name     : " + projectToDelete.getName());
        System.out.println(" result   : " + result);
        System.out.println("-------------------------------------------------"); 
        
        return projectToDelete;
        
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Project> deleteProjectAsync(final String id) {

        ListenableFuture<Project> future = this.service.submit(new Callable<Project>() {
            @Override
            public Project call() throws Exception {
                return deleteProject(id);
            }
        });

        return future;
    }

}