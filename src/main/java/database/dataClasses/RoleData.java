package database.dataClasses;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import database.Database;
import org.jetbrains.annotations.NotNull;

public class RoleData implements Cloneable {
    private final Database database;

    private final String name;
    private final String id;
    private int priority;

    public RoleData(@NotNull Database database, @NotNull String name, @NotNull String id, int priority) {
        this.database = database;
        this.name = name;
        this.id = id;
        this.priority = priority;
    }

    // Uncommment if needed
    /*public void updateRole(String name, String id, int priority) {
        RoleData clone = tryClone();

        this.name = name;
        this.id = id;
        this.priority = priority;

        update(clone);
    }*/

    public void updateRole(int priority) {
        RoleData clone = tryClone();

        this.priority = priority;

        update(clone);
    }

    public void deleteDocument() {
        BasicDBObject document = getRoleDocument();
        DBCollection dbCollection = this.database.getCollection("roles");

        dbCollection.remove(document);
    }

    //public String getName() { return this.name; }
    public String getId() { return this.id; }

    private BasicDBObject getRoleDocument() {
        return new BasicDBObject()
                .append("name", this.name)
                .append("id", this.id)
                .append("priority", this.priority);
    }

    private void update(RoleData clone) {
        BasicDBObject cloneObject = clone.getRoleDocument();
        BasicDBObject role = this.getRoleDocument();

        DBCollection roleCollection = this.database.getCollection("roles");

        Database.update(roleCollection, cloneObject, role);
    }

    private RoleData tryClone() {
        try {
            return (RoleData) this.clone();
        } catch(Exception e) {
            return null;
        }
    }
}
