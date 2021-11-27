package database;

import com.mongodb.*;
import com.mongodb.client.model.DBCollectionUpdateOptions;

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
    // If a document doesn't exist within the database, it gets created instead.
    private WriteResult update(DBCollection collection, DBObject filter, DBObject update) {
        DBCollectionUpdateOptions updateOptions = new DBCollectionUpdateOptions();
        updateOptions.upsert(true);

        return collection.update(filter, update, updateOptions);
    }

    private DBObject search(DBCollection collection, DBObject searchParams) {
        BasicDBObject query = new BasicDBObject();

        for (String key : searchParams.keySet()) {
            query.put(key, searchParams.get(key));
        }

        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) return cursor.next();
        return null;
    }

    public DBObject getRole(DBObject searchParams) {
        DBCollection collection = this.database.getCollection("roles");
        return search(collection, searchParams);
    }

    public WriteResult updateRole(DBObject filter, DBObject update) {
        DBCollection collection = this.database.getCollection("roles");
        return update(collection, filter, update);
    }

    public void deleteRole(DBObject role) {
        DBCollection collection = this.database.getCollection("roles");
        if (role == null) return;

        collection.remove(role);
    }
}
