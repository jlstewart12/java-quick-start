package com.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.InsertManyOptions;
import com.mongodb.diagnostics.logging.Loggers;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

public class Create {

    private static final Random rand = new Random();

    public static void main(String[] args) {
        Logger.getLogger(Loggers.PREFIX).setLevel(Level.WARNING);
        try (MongoClient mongoClient = MongoClients.create(System.getProperty("mongodb.uri"))) {

            MongoDatabase sampleTrainingDB = mongoClient.getDatabase("sample_training");
            MongoCollection<Document> gradesCollection = sampleTrainingDB.getCollection("grades");

            insertOneDocument(gradesCollection);
            insertManyDocuments(gradesCollection);
        }
    }

    private static void insertOneDocument(MongoCollection<Document> gradesCollection) {
        try {
            gradesCollection.insertOne(generateNewGrade(10000d, 1d));
            System.out.println("One document inserted.");
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    private static void insertManyDocuments(MongoCollection<Document> gradesCollection) {
        List<Document> grades = new ArrayList<>();
        for (int classId = 1; classId <= 10; classId++) {
            grades.add(generateNewGrade(10001d, classId));
        }

        try {
            gradesCollection.insertMany(grades, new InsertManyOptions().ordered(false));
            System.out.println("Ten documents inserted.");
        } catch (MongoException e) {
            e.printStackTrace();
        }
    }

    private static Document generateNewGrade(double studentId, double classId) {
        List<Document> scores = asList(new Document("type", "exam").append("score", rand.nextDouble() * 100),
                                       new Document("type", "quiz").append("score", rand.nextDouble() * 100),
                                       new Document("type", "homework").append("score", rand.nextDouble() * 100),
                                       new Document("type", "homework").append("score", rand.nextDouble() * 100));
        return new Document("_id", new ObjectId()).append("student_id", studentId)
                                                  .append("class_id", classId)
                                                  .append("scores", scores);
    }
}
