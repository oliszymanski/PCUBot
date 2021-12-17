package database.dataClasses;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import database.Database;
import org.jetbrains.annotations.NotNull;

public class RoleData {
    private final Database database;

    private final String name;
    private final String id;
    private int requiredLevel;

    public RoleData(@NotNull Database database, @NotNull String name, @NotNull String id, int requiredLevel) {
        this.database = database;
        this.name = name;
        this.id = id;
        this.requiredLevel = requiredLevel;
    }

    public RoleData(@NotNull Database database, @NotNull String name, @NotNull String id) {
        this.database = database;
        this.name = name;
        this.id = id;
        this.requiredLevel = 1;
    }

    public static RoleData createNewRole(Database database, String roleId, String name) {
        BasicDBObject userObject = new BasicDBObject()
                .append("name", name)
                .append("id", roleId)
                .append("requiredLevel", 1);

        DBCollection userCollection = database.getCollection("roles");
        Database.update(userCollection, userObject, userObject);

        return new RoleData(database, name, roleId);
    }

    public void setLevel(int level) {
        this.requiredLevel = level;
        updateDocument();
    }

    public void deleteDocument() {
        BasicDBObject document = getRoleDocument();
        DBCollection dbCollection = this.database.getCollection("roles");

        dbCollection.remove(document);
    }

    //public String getName() { return this.name; }
    public String getId() { return this.id; }
    public int getRequiredLevel() { return this.requiredLevel; }

    private BasicDBObject getRoleDocument() {
        return new BasicDBObject()
                .append("name", this.name)
                .append("id", this.id)
                .append("requiredLevel", this.requiredLevel);
    }

    private void updateDocument() {
        DBCollection roleCollection = this.database.getCollection("roles");

        BasicDBObject roleQuery = (BasicDBObject) roleCollection.find(new BasicDBObject("id", this.id)).next();

        BasicDBObject update = getRoleDocument();

        Database.update(roleCollection, roleQuery, update);
    }
}
