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
import com.misys.async_rest.model.Commit;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class CommitDao {

	private ListeningExecutorService service;

    // ---------------------------------------------------------------------------------------------------

    public CommitDao() {

        this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public Collection<Commit> getCommits() {
        
        Map<String, Commit> commits = new ConcurrentHashMap<String, Commit>();
        
        for (Document cursor : Main.database.getCommits().find()) {
            Commit commit = new Commit();
            commit.setId(cursor.get("id").toString());
            commit.setName(cursor.get("name").toString());
            commits.put(commit.getId(), commit);
        }
        
        return commits.values();
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public ListenableFuture<Collection<Commit>> getCommitsAsync() {

        ListenableFuture<Collection<Commit>> future = this.service
                .submit(new Callable<Collection<Commit>>() {
                    @Override
                    public Collection<Commit> call() throws Exception {
                        return getCommits();
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
    public Commit getCommit(String id) {

        Document document = Main.database.getCommits().find(eq("id", id)).first();
        Commit commit = new Commit();
        
        commit.setId(document.get("id").toString());
        commit.setName(document.get("name").toString());
        
        System.out.println("\n------------- GET COMMIT WITH ID ----------------");
        System.out.println(" id   : " + document.get("id"));
        System.out.println(" name : " + document.get("name"));
        System.out.println("-------------------------------------------------");       
        
        return commit;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Commit> getCommitAsync(final String id) {

        ListenableFuture<Commit> future = this.service.submit(new Callable<Commit>() {
            @Override
            public Commit call() throws Exception {
                return getCommit(id);
            }
        });

        return future;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param commit
     * @return
     */
    public Commit addCommit(Commit commit) {

        commit.setId(UUID.randomUUID().toString());
        
        Document document = new Document();
        document.put("id", commit.getId());
        document.put("name", commit.getName());
        
        Main.database.getCommits().insertOne(document);

        return commit;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param commit
     * @return
     */
    public ListenableFuture<Commit> addCommitAsync(final Commit commit) {

        ListenableFuture<Commit> future = this.service.submit(new Callable<Commit>() {
            @Override
            public Commit call() throws Exception {
                return addCommit(commit);
            }
        });

        return future;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param commit
     * @return
     */
    public Commit updateCommit(Commit commit) {

        String commitId = commit.getId();
        String commitName = commit.getName();
        
        Map<String, Object> changes = new ConcurrentHashMap<String, Object>();
        changes.put("id", commitId);
        changes.put("name", commitName);
        
        UpdateResult result = Main.database.getCommits().updateOne(eq("id", commit.getId()), new Document("$set", new Document(changes)));
        
        System.out.println("\n------------- UPDATE COMMIT WITH ID -------------");
        System.out.println(" id     : " + commitId);
        System.out.println(" name   : " + commitName);
        System.out.println(" result :" + result);
        System.out.println("-------------------------------------------------");       
        
        return commit;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param commit
     * @return
     */
    public ListenableFuture<Commit> updateCommitAsync(final Commit commit) {

        ListenableFuture<Commit> future = this.service.submit(new Callable<Commit>() {
            @Override
            public Commit call() throws Exception {
                return updateCommit(commit);
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
    public Commit deleteCommit(String id) {

        Commit commitToDelete = getCommit(id);
        DeleteResult result = Main.database.getCommits().deleteOne(eq("id", id));
        
        System.out.println("\n------------- DELETE COMMIT WITH ID -------------");
        System.out.println(" id       : " + id);
        System.out.println(" name     : " + commitToDelete.getName());
        System.out.println(" result   : " + result);
        System.out.println("-------------------------------------------------"); 
        
        return commitToDelete;
        
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Commit> deleteCommitAsync(final String id) {

        ListenableFuture<Commit> future = this.service.submit(new Callable<Commit>() {
            @Override
            public Commit call() throws Exception {
                return deleteCommit(id);
            }
        });

        return future;
    }

}