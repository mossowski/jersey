package com.misys.async_rest.dao;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {

    public static final String              IP   = "localhost";
    public static final int                 PORT = 27017;
    public static MongoClient               mongoClient;
    public static MongoDatabase             mongoDatabase;
    public static MongoCollection<Document> persons;

    public Database() {

        try {

            mongoClient = new MongoClient(IP, PORT);
            mongoDatabase = mongoClient.getDatabase("marcin");
            persons = mongoDatabase.getCollection("persons");

            // System.out.println("Added " + persons.count() + " to database!");

        }
        catch (MongoException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {

        mongoClient.close();
    }

}
