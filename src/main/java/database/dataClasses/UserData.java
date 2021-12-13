package database.dataClasses;

import bot.Bot;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import database.Database;
import org.bson.types.ObjectId;
import widgets.SimpleEmbed;

import java.util.ArrayList;

public class UserData implements Cloneable {
    private final Database database;
    private final String id;
    private int level;
    private int currentExp;
    private int expUntilNext;
    private final ArrayList<ObjectId> warnings;

    public UserData(Database database, String id, ArrayList<ObjectId> warnings, int level, int current_exp, int exp_until_next) {
        this.database = database;
        this.id = id;
        this.level = level;
        this.currentExp = current_exp;
        this.expUntilNext = exp_until_next;
        this.warnings = warnings;
    }

    public UserData(Database database, String id) {
        this.database = database;
        this.id = id;
        this.level = 1;
        this.currentExp = 0;
        this.expUntilNext = calculateNextExp(1);
        this.warnings = new ArrayList<>();
    }

    public static UserData createNewUser(Database database, String userId) {
        BasicDBObject userObject = new BasicDBObject()
                .append("id", userId)
                .append("warnings", new ArrayList<>())
                .append("level", 1)
                .append("currentExp", 0)
                .append("expUntilNext", calculateNextExp(1));

        DBCollection userCollection = database.getCollection("users");
        Database.update(userCollection, userObject, userObject);

        return new UserData(database, userId);
    }

    private void update(int exp) {
        DBCollection userCollection = database.getCollection("users");

        BasicDBObject userQuery = (BasicDBObject) userCollection.find(new BasicDBObject("id", this.id)).next();

        this.currentExp += exp;
        System.out.println(currentExp);
        if (this.currentExp >= expUntilNext) levelUp();

        BasicDBObject query = getUserDocument();


        Database.update(userCollection, userQuery, query);

    }

    // TODO: Refactor this later
    public void addWarning(WarningData warningData) {
        DBCollection userCollection = database.getCollection("users");

        BasicDBObject userDocument = (BasicDBObject) userCollection.find(getUserDocument()).next();

        this.warnings.add(warningData.getId());
        BasicDBObject query = getUserDocument();

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
                .append("warnings", this.warnings)
                .append("level", this.level)
                .append("currentExp", this.currentExp)
                .append("expUntilNext", this.expUntilNext);
    }

    public void giveExp(int exp) {
        update(exp);
    }

    private void levelUp() {
        this.level += 1;
        this.currentExp = 0;
        this.expUntilNext = calculateNextExp(this.level);

        if (this.level - 1 != 0) {
            Bot.getJda().openPrivateChannelById(this.id).queue(privateChannel -> {
                String widgetText = String.format("Your level has advanced from %d to %d!", this.level - 1, this.level);
                SimpleEmbed simpleEmbed = new SimpleEmbed("Level Up", widgetText);
                privateChannel.sendMessageEmbeds(simpleEmbed.build()).queue();
            });
        }
    }

    public static int calculateNextExp(int level) {
        return (int) Math.floor(((Math.sqrt((double) level) * 25) / 4) * 10);
    }

    public int getLevel() { return this.level; }
    public int getCurrentExp() { return this.currentExp; }
    public int getExpUntilNext() { return this.expUntilNext; }
}
