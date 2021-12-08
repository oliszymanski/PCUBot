package database.dataClasses;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import database.Database;
import org.bson.types.ObjectId;

import java.util.ArrayList;

public class UserData implements Cloneable {
    private final Database database;
    private final String id;
    private final ArrayList<ObjectId> warnings;

    public UserData(Database database, String id, ArrayList<ObjectId> warnings) {
        this.database = database;
        this.id = id;
        this.warnings = warnings;
    }

    public UserData(Database database, String id) {
        this.database = database;
        this.id = id;
        this.warnings = new ArrayList<>();
    }

    public static UserData createNewUser(Database database, String userId) {
        BasicDBObject userObject = new BasicDBObject()
                .append("id", userId)
                .append("warnings", new ArrayList<>());

        DBCollection userCollection = database.getCollection("users");
        Database.update(userCollection, userObject, userObject);

        return new UserData(database, userId);
    }

    // TODO: Refactor this later
    public void addWarning(WarningData warningData) {
        DBCollection userCollection = database.getCollection("users");

        BasicDBObject userDocument = (BasicDBObject) userCollection.find(getUserDocument()).next();

        this.warnings.add(warningData.getId());
        BasicDBObject query = new BasicDBObject().append("id", this.id).append("warnings", this.warnings);

        Database.update(userCollection, userDocument, query);
    }

    public void deleteDocument() {
        BasicDBObject document = getUserDocument();
        DBCollection dbCollection = this.database.getCollection("users");

        for (ObjectId id : this.warnings) {
            this.database.getWarningById(id).deleteDocument();
        }

        dbCollection.remove(document);
    }

    private BasicDBObject getUserDocument() {
        return new BasicDBObject()
                .append("id", this.id)
                .append("warnings", this.warnings);
    }

    private UserData tryClone() {
        try {
            return (UserData) this.clone();
        } catch(Exception e) {
            return null;
        }
    }
}
