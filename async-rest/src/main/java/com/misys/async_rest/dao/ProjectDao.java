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
import com.misys.async_rest.model.Project;

public class ProjectDao {

    private Map<String, Project> projects;
    private ListeningExecutorService service;

    // ---------------------------------------------------------------------------------------------------

    public ProjectDao() {

        projects = new ConcurrentHashMap<String, Project>();
        service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public Collection<Project> getProjects() {
        return projects.values();
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public ListenableFuture<Collection<Project>> getProjectsAsync() {

        ListenableFuture<Collection<Project>> future = service
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
        return projects.get(id);
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Project> getProjectAsync(final String id) {

        ListenableFuture<Project> future = service.submit(new Callable<Project>() {
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
        projects.put(project.getId(), project);

        return project;
    }

    // ---------------------------------------------------------------------------------------------------

    public ListenableFuture<Project> addProjectAsync(final Project project) {

        ListenableFuture<Project> future = service.submit(new Callable<Project>() {
            @Override
            public Project call() throws Exception {
                return addProject(project);
            }
        });

        return future;
    }

}
