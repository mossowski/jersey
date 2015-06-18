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
import com.misys.async_rest.model.Job;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class JobDao {

	private ListeningExecutorService service;

    // ---------------------------------------------------------------------------------------------------

    public JobDao() {

        this.service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public Collection<Job> getJobs() {
        
        Map<String, Job> jobs = new ConcurrentHashMap<String, Job>();
        
        for (Document cursor : Main.database.getJobs().find()) {
            Job job = new Job();
            job.setId(cursor.get("id").toString());
            job.setName(cursor.get("name").toString());
            jobs.put(job.getId(), job);
        }
        
        return jobs.values();
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     */
    public ListenableFuture<Collection<Job>> getJobsAsync() {

        ListenableFuture<Collection<Job>> future = this.service
                .submit(new Callable<Collection<Job>>() {
                    @Override
                    public Collection<Job> call() throws Exception {
                        return getJobs();
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
    public Job getJob(String id) {

        Document document = Main.database.getJobs().find(eq("id", id)).first();
        Job job = new Job();
        
        job.setId(document.get("id").toString());
        job.setName(document.get("name").toString());
        
        System.out.println("\n------------- GET JOB WITH ID ----------------");
        System.out.println(" id   : " + document.get("id"));
        System.out.println(" name : " + document.get("name"));
        System.out.println("-------------------------------------------------");       
        
        return job;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Job> getJobAsync(final String id) {

        ListenableFuture<Job> future = this.service.submit(new Callable<Job>() {
            @Override
            public Job call() throws Exception {
                return getJob(id);
            }
        });

        return future;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param job
     * @return
     */
    public Job addJob(Job job) {

        job.setId(UUID.randomUUID().toString());
        
        Document document = new Document();
        document.put("id", job.getId());
        document.put("name", job.getName());
        
        Main.database.getJobs().insertOne(document);

        return job;
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param job
     * @return
     */
    public ListenableFuture<Job> addJobAsync(final Job job) {

        ListenableFuture<Job> future = this.service.submit(new Callable<Job>() {
            @Override
            public Job call() throws Exception {
                return addJob(job);
            }
        });

        return future;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param job
     * @return
     */
    public Job updateJob(Job job) {

        String jobId = job.getId();
        String jobName = job.getName();
        
        Map<String, Object> changes = new ConcurrentHashMap<String, Object>();
        changes.put("id", jobId);
        changes.put("name", jobName);
        
        UpdateResult result = Main.database.getJobs().updateOne(eq("id", job.getId()), new Document("$set", new Document(changes)));
        
        System.out.println("\n------------- UPDATE JOB WITH ID -------------");
        System.out.println(" id     : " + jobId);
        System.out.println(" name   : " + jobName);
        System.out.println(" result :" + result);
        System.out.println("-------------------------------------------------");       
        
        return job;
    }
    
    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param job
     * @return
     */
    public ListenableFuture<Job> updateJobAsync(final Job job) {

        ListenableFuture<Job> future = this.service.submit(new Callable<Job>() {
            @Override
            public Job call() throws Exception {
                return updateJob(job);
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
    public Job deleteJob(String id) {

        Job jobToDelete = getJob(id);
        DeleteResult result = Main.database.getJobs().deleteOne(eq("id", id));
        
        System.out.println("\n------------- DELETE JOB WITH ID -------------");
        System.out.println(" id       : " + id);
        System.out.println(" name     : " + jobToDelete.getName());
        System.out.println(" result   : " + result);
        System.out.println("-------------------------------------------------"); 
        
        return jobToDelete;
        
    }

    // ---------------------------------------------------------------------------------------------------

    /**
     * 
     * @param id
     * @return
     */
    public ListenableFuture<Job> deleteJobAsync(final String id) {

        ListenableFuture<Job> future = this.service.submit(new Callable<Job>() {
            @Override
            public Job call() throws Exception {
                return deleteJob(id);
            }
        });

        return future;
    }

}