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
import com.misys.async_rest.model.Build;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class BuildDao {

	private ListeningExecutorService service;

    // ---------------------------------------------------------------------------------------------------

    public BuildDao() {

        this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public Collection<Build> getBuilds() {
        
        Map<String, Build> builds = new ConcurrentHashMap<String, Build>();
        
        for (Document cursor : Main.database.getBuilds().find()) {
            Build build = new Build();
            build.setId(cursor.get("id").toString());
            build.setName(cursor.get("name").toString());
            builds.put(build.getId(), build);
        }
        
        return builds.values();
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public ListenableFuture<Collection<Build>> getBuildsAsync() {

        ListenableFuture<Collection<Build>> future = this.service
                .submit(new Callable<Collection<Build>>() {
                    @Override
                    public Collection<Build> call() throws Exception {
                        return getBuilds();
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
    public Build getBuild(String id) {

        Document document = Main.database.getBuilds().find(eq("id", id)).first();
        Build build = new Build();
        
        build.setId(document.get("id").toString());
        build.setName(document.get("name").toString());
        
        System.out.println("\n------------- GET BUILD WITH ID ----------------");
        System.out.println(" id   : " + document.get("id"));
        System.out.println(" name : " + document.get("name"));
        System.out.println("-------------------------------------------------");       
        
        return build;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Build> getBuildAsync(final String id) {

        ListenableFuture<Build> future = this.service.submit(new Callable<Build>() {
            @Override
            public Build call() throws Exception {
                return getBuild(id);
            }
        });

        return future;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param build
     * @return
     */
    public Build addBuild(Build build) {

        build.setId(UUID.randomUUID().toString());
        
        Document document = new Document();
        document.put("id", build.getId());
        document.put("name", build.getName());
        
        Main.database.getBuilds().insertOne(document);

        return build;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param build
     * @return
     */
    public ListenableFuture<Build> addBuildAsync(final Build build) {

        ListenableFuture<Build> future = this.service.submit(new Callable<Build>() {
            @Override
            public Build call() throws Exception {
                return addBuild(build);
            }
        });

        return future;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param build
     * @return
     */
    public Build updateBuild(Build build) {

        String buildId = build.getId();
        String buildName = build.getName();
        
        Map<String, Object> changes = new ConcurrentHashMap<String, Object>();
        changes.put("id", buildId);
        changes.put("name", buildName);
        
        UpdateResult result = Main.database.getBuilds().updateOne(eq("id", build.getId()), new Document("$set", new Document(changes)));
        
        System.out.println("\n------------- UPDATE BUILD WITH ID -------------");
        System.out.println(" id     : " + buildId);
        System.out.println(" name   : " + buildName);
        System.out.println(" result :" + result);
        System.out.println("-------------------------------------------------");       
        
        return build;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param build
     * @return
     */
    public ListenableFuture<Build> updateBuildAsync(final Build build) {

        ListenableFuture<Build> future = this.service.submit(new Callable<Build>() {
            @Override
            public Build call() throws Exception {
                return updateBuild(build);
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
    public Build deleteBuild(String id) {

        Build buildToDelete = getBuild(id);
        DeleteResult result = Main.database.getBuilds().deleteOne(eq("id", id));
        
        System.out.println("\n------------- DELETE BUILD WITH ID -------------");
        System.out.println(" id       : " + id);
        System.out.println(" name     : " + buildToDelete.getName());
        System.out.println(" result   : " + result);
        System.out.println("-------------------------------------------------"); 
        
        return buildToDelete;
        
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Build> deleteBuildAsync(final String id) {

        ListenableFuture<Build> future = this.service.submit(new Callable<Build>() {
            @Override
            public Build call() throws Exception {
                return deleteBuild(id);
            }
        });

        return future;
    }

}