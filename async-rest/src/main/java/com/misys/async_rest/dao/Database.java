package com.misys.async_rest.dao;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Database {

    public Database() {

        try {

            MongoClient mongo = new MongoClient("localhost", 27017);
            MongoDatabase db = mongo.getDatabase("marcin");
            MongoCollection<Document> tableCollection = db.getCollection("persons");

            Document document = new Document();
            document.put("id", "1");
            document.put("name", "Wiesiek");
            tableCollection.insertOne(document);

            mongo.close();

        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

}
