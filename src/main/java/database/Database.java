package database;

import com.mongodb.*;
import com.mongodb.client.model.DBCollectionUpdateOptions;

import java.util.HashMap;

public class Database {
    private final DB database;

    public Database(String address) {
        MongoClient mongoClient = new MongoClient(address, 27017);
        if (mongoClient == null) {
            System.out.println("There was a problem with connecting to the MongoDB client. Ensure it is running or you've connected to the correct address.");
            System.exit(1);
        }

        // Ignore the deprecation warning.
        this.database = mongoClient.getDB("PCUBot");

    }

    // Just a simple wrapper function
    private WriteResult update(DBCollection collection, DBObject filter, DBObject update) {
        DBCollectionUpdateOptions updateOptions = new DBCollectionUpdateOptions();
        updateOptions.upsert(true);

        return collection.update(filter, update, updateOptions);
    }

    private DBObject search(DBCollection collection, HashMap<String, String> searchParams) {
        BasicDBObject query = new BasicDBObject();

        for (String key : searchParams.keySet()) {
            query.put(key, searchParams.get(key));
        }

        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) return cursor.next();
        return null;
    }

    public DBObject getRole(HashMap<String, String> searchParams) {
        DBCollection collection = this.database.getCollection("roles");
        return search(collection, searchParams);
    }
}
