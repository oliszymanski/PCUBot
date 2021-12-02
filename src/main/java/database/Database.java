package database;

import com.mongodb.*;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import database.dataClasses.RoleData;
import database.dataClasses.UserData;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;

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

    // If a document doesn't exist within the database, it gets created instead.
    public static void update(DBCollection collection, DBObject filter, DBObject update) {
        DBCollectionUpdateOptions updateOptions = new DBCollectionUpdateOptions();
        updateOptions.upsert(true);

        collection.update(filter, update, updateOptions);
    }

    // Forcefully shove a new document, even if it contains the same data.
    public static void insert(DBCollection collection, DBObject object) {
        collection.insert(object);
    }

    public DBCollection getCollection(String name) {
        return this.database.getCollection(name);
    }

    public DBObject search(DBCollection collection, DBObject searchParams) {
        BasicDBObject query = new BasicDBObject();

        for (String key : searchParams.keySet()) {
            query.put(key, searchParams.get(key));
        }

        DBCursor cursor = collection.find(query);
        if (cursor.hasNext()) return cursor.next();
        return null;
    }

    public RoleData getRole(DBObject searchParams) {
        DBCollection collection = this.database.getCollection("roles");
        DBObject result = search(collection, searchParams);
        if (result == null) return null;

        return new RoleData(
                this,
                (String) result.get("name"),
                (String) result.get("id"),
                (int) result.get("priority")
        );
    }

    public UserData getUser(String userId) {
        DBCollection collection = this.database.getCollection("users");
        BasicDBObject user = new BasicDBObject().append("id", userId);

        DBObject result = search(collection, user);
        System.out.println(result);

        if (result == null) return null;

        Document docResult = new Document(result.toMap());
        ArrayList<ObjectId> warningResults = (ArrayList<ObjectId>) docResult.getList("warnings", ObjectId.class);

        return new UserData(this, userId, warningResults);
    }

    public UserData getOrCreateUser(String userId) {
        UserData userData = getUser(userId);
        if (userData == null) return UserData.createNewUser(this, userId);

        return userData;
    }

}
