package com.misys.async_rest.dao;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {

    private static final String       IP   = "localhost";
    private static final int          PORT = 27017;
    private MongoClient               mongoClient;
    private MongoDatabase             mongoDatabase;
    private MongoCollection<Document> persons;
    private MongoCollection<Document> projects;
    private MongoCollection<Document> products;
    private MongoCollection<Document> commits;
    private MongoCollection<Document> builds;
    private MongoCollection<Document> jobs;

	public Database() {

        try {

            this.mongoClient = new MongoClient(IP, PORT);
            this.mongoDatabase = this.mongoClient.getDatabase("marcin");
            this.setPersons(this.mongoDatabase.getCollection("persons"));

        }
        catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public MongoCollection<Document> getPersons() {
		return this.persons;
	}

	public void setPersons(MongoCollection<Document> aPersons) {
		this.persons = aPersons;
	}
	
	public MongoCollection<Document> getProjects() {
		return this.projects;
	}

	public void setProjects(MongoCollection<Document> aProjects) {
		this.projects = aProjects;
	}

	public MongoCollection<Document> getProducts() {
		return this.products;
	}

	public void setProducts(MongoCollection<Document> aProducts) {
		this.products = aProducts;
	}

	public MongoCollection<Document> getCommits() {
		return this.commits;
	}

	public void setCommits(MongoCollection<Document> aCommits) {
		this.commits = aCommits;
	}

	public MongoCollection<Document> getBuilds() {
		return this.builds;
	}

	public void setBuilds(MongoCollection<Document> aBuilds) {
		this.builds = aBuilds;
	}

	public MongoCollection<Document> getJobs() {
		return this.jobs;
	}

	public void setJobs(MongoCollection<Document> aJobs) {
		this.jobs = aJobs;
	}
	
	public void closeConnection() {

        this.mongoClient.close();
    }

}
