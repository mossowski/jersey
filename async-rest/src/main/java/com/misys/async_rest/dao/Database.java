package com.misys.async_rest.dao;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {

    public static final String              IP   = "localhost";
    public static final int                 PORT = 27017;
    public        MongoClient               mongoClient;
    public  	  MongoDatabase             mongoDatabase;
    public 		  MongoCollection<Document> persons;

    public Database() {

        try {

            this.mongoClient = new MongoClient(IP, PORT);
            this.mongoDatabase = this.mongoClient.getDatabase("marcin");
            this.persons = this.mongoDatabase.getCollection("persons");

            // System.out.println("Added " + persons.count() + " to database!");

        }
        catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {

        this.mongoClient.close();
    }

}
