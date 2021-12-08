package database.dataClasses;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import database.Database;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.Date;

public class WarningData {
    private final Database database;

    private final ObjectId objectId;
    private final String reason;
    private final Date date;

    public WarningData(ObjectId objectId, Database database, String reason, Date date) {
        this.objectId = objectId;
        this.database = database;
        this.reason = reason;
        this.date = date;
    }

    public static WarningData createNewWarning(Database database, String reason) {
        Date date = Date.from(Instant.now());
        BasicDBObject warningObject = new BasicDBObject()
                .append("reason", reason)
                .append("date", date);

        DBCollection warningCollection = database.getCollection("warnings");
        Database.insert(warningCollection, warningObject);

        BasicDBObject search = (BasicDBObject) database.search(warningCollection, warningObject);

        return new WarningData((ObjectId) search.get("_id"), database, reason, date);
    }

    public void deleteDocument() {
        BasicDBObject document = getWarningDocument();
        DBCollection dbCollection = this.database.getCollection("warnings");

        dbCollection.remove(document);
    }

    private BasicDBObject getWarningDocument() {
        return new BasicDBObject()
                .append("_id", this.objectId)
                .append("reason", this.reason)
                .append("date", this.date);
    }

    public ObjectId getId() { return objectId; }
}
