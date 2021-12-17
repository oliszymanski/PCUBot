package database;

import com.mongodb.*;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import database.dataClasses.RoleData;
import database.dataClasses.UserData;
import database.dataClasses.WarningData;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class Database {
    private final DB database;

    @SuppressWarnings("deprecation")
    public Database(String address) {
        MongoClient mongoClient = new MongoClient(address, 27017);
        if (mongoClient == null) {
            System.out.println("There was a problem with connecting to the MongoDB client. Ensure it is running or you've connected to the correct address.");
            System.exit(1);
        }

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

    public RoleData getRole(String roleId) {
        DBCollection collection = this.database.getCollection("roles");
        BasicDBObject role = new BasicDBObject().append("id", roleId);

        DBObject result = search(collection, role);

        if (result == null) return null;

        return new RoleData(this, (String) result.get("name"), roleId, (int) result.get("requiredLevel"));
    }

    public RoleData getRole(int level) {
        DBCollection collection = this.database.getCollection("roles");
        BasicDBObject role = new BasicDBObject().append("requiredLevel", level);

        DBObject result = search(collection, role);

        if (result == null) return null;

        return new RoleData(this, (String) result.get("name"), (String) result.get("id"), (int) result.get("requiredLevel"));
    }

    public ArrayList<RoleData> getRolesLte(int level) {
        DBCollection collection = this.database.getCollection("roles");
        BasicDBObject role = new BasicDBObject("requiredLevel", new BasicDBObject("$lte", level));

        DBCursor results = collection.find(role).sort(new BasicDBObject("requiredLevel", -1));
        ArrayList<RoleData> roleList = new ArrayList<>();
        while (results.hasNext()) {
            DBObject result = results.next();
            roleList.add(new RoleData(this, (String) result.get("name"), (String) result.get("id"), (int) result.get("requiredLevel")));
        }
        return roleList;
    }

    public RoleData getOrCreateRole(String roleId, String name) {
        RoleData roleData = getRole(roleId);
        if (roleData == null) return RoleData.createNewRole(this, roleId, name);

        return roleData;
    }

    public UserData getUser(String userId) {
        DBCollection collection = this.database.getCollection("users");
        BasicDBObject user = new BasicDBObject().append("id", userId);

        DBObject result = search(collection, user);

        if (result == null) return null;

        // Suppress because there is no good way to solve this warning
        @SuppressWarnings("unchecked")
        Document docResult = new Document(result.toMap());
        ArrayList<ObjectId> warningResults = (ArrayList<ObjectId>) docResult.getList("warnings", ObjectId.class);

        return new UserData(this, userId, warningResults, (int) result.get("level"), (int) result.get("currentExp"), (int) result.get("expUntilNext"), (int) result.get("warningsUntilPunishment"));
    }

    public WarningData getWarningById(ObjectId id) {
        DBCollection collection = this.database.getCollection("warnings");
        BasicDBObject warningQuery = new BasicDBObject("_id", id);

        DBObject result = search(collection, warningQuery);
        if (result == null) return null;

        return new WarningData(id, this, (String) result.get("reason"), (Date) result.get("date"));
    }

    public UserData getOrCreateUser(String userId) {
        UserData userData = getUser(userId);
        if (userData == null) return UserData.createNewUser(this, userId);

        return userData;
    }

}
