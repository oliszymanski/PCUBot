package database.dataClasses;

import bot.Bot;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import database.Database;
import net.dv8tion.jda.api.entities.Guild;
import org.bson.types.ObjectId;
import widgets.SimpleEmbed;

import java.util.ArrayList;

public class UserData {
    private final Database database;
    private final String id;
    private int level;
    private int currentExp;
    private int expUntilNext;
    private int warningsUntilPunishment;
    private final ArrayList<ObjectId> warnings;

    private static final int DEFAULT_WARNINGS = 2;

    public UserData(Database database, String id, ArrayList<ObjectId> warnings, int level, int currentExp, int expUntilNext, int warningsUntilPunishment) {
        this.database = database;
        this.id = id;
        this.level = level;
        this.currentExp = currentExp;
        this.expUntilNext = expUntilNext;
        this.warningsUntilPunishment = warningsUntilPunishment;
        this.warnings = warnings;
    }

    public UserData(Database database, String id) {
        this.database = database;
        this.id = id;
        this.level = 1;
        this.currentExp = 0;
        this.expUntilNext = calculateNextExp(1);
        this.warningsUntilPunishment = DEFAULT_WARNINGS;
        this.warnings = new ArrayList<>();
    }

    public static UserData createNewUser(Database database, String userId) {
        BasicDBObject userObject = new BasicDBObject()
                .append("id", userId)
                .append("warnings", new ArrayList<>())
                .append("warningsUntilPunishment", DEFAULT_WARNINGS)
                .append("level", 1)
                .append("currentExp", 0)
                .append("expUntilNext", calculateNextExp(1));

        DBCollection userCollection = database.getCollection("users");
        Database.update(userCollection, userObject, userObject);

        return new UserData(database, userId);
    }

    private void updateDocument() {
        DBCollection userCollection = database.getCollection("users");

        BasicDBObject userQuery = (BasicDBObject) userCollection.find(new BasicDBObject("id", this.id)).next();

        BasicDBObject query = getUserDocument();

        Database.update(userCollection, userQuery, query);
    }

    public void addWarning(WarningData warningData) {
        this.warnings.add(warningData.getId());
        this.warningsUntilPunishment--;
        if (this.warningsUntilPunishment == 0) {
            this.warningsUntilPunishment = DEFAULT_WARNINGS;
            // Take away the user's role and set back their level here.
        }
        updateDocument();
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
                .append("warningsUntilPunishment", this.warningsUntilPunishment)
                .append("warnings", this.warnings)
                .append("level", this.level)
                .append("currentExp", this.currentExp)
                .append("expUntilNext", this.expUntilNext);
    }

    public void giveExp(int exp, Guild guild) {
        this.currentExp += exp;
        if (this.currentExp >= expUntilNext) levelUp(guild);
        updateDocument();
    }

    private void levelUp(Guild guild) {
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

        ArrayList<RoleData> role = database.getRolesLte(this.level);
        for (RoleData roleData : role) {
            guild.addRoleToMember(this.id, guild.getRoleById(roleData.getId())).queue();
        }
    }

    public static int calculateNextExp(int level) {
        return (int) Math.floor(((Math.sqrt(level) * 25) / 4) * 10);
    }

    public int getLevel() { return this.level; }
    public int getCurrentExp() { return this.currentExp; }
    public int getExpUntilNext() { return this.expUntilNext; }
}
