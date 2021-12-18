package database.dataClasses;

import bot.Bot;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import database.Database;

public class SettingsData {
    private final String guildId;
    private String welcomeChannelId;
    private String goodbyeChannelId;
    private String levelUpChannelId;

    public SettingsData(String guildId, String welcomeChannelId, String goodbyeChannelId, String levelUpChannelId) {
        this.guildId = guildId;
        this.welcomeChannelId = welcomeChannelId;
        this.goodbyeChannelId = goodbyeChannelId;
        this.levelUpChannelId = levelUpChannelId;
    }

    private SettingsData(String guildId) {
        this.guildId = guildId;
        this.welcomeChannelId = null;
        this.goodbyeChannelId = null;
        this.levelUpChannelId = null;
    }

    public static SettingsData createNewSettings(String guildId) {
        BasicDBObject settings = new BasicDBObject()
                .append("guildId", guildId)
                .append("welcomeChannelId", null)
                .append("goodbyeChannelId", null)
                .append("levelUpChannelId", null);

        DBCollection settingsCollection = Bot.getDatabase().getCollection("settings");
        Database.update(settingsCollection, settings, settings);

        return new SettingsData(guildId);
    }

    private void updateDocument() {
        DBCollection settingsCollection = Bot.getDatabase().getCollection("settings");

        BasicDBObject settingsQuery = (BasicDBObject) settingsCollection.find(new BasicDBObject("guildId", this.guildId)).next();

        BasicDBObject query = getSettingsDocument();

        Database.update(settingsCollection, settingsQuery, query);
    }

    public void setWelcomeChannelId(String welcomeChannelId) {
        this.welcomeChannelId = welcomeChannelId;
        updateDocument();
    }

    public void setGoodbyeChannelId(String goodbyeChannelId) {
        this.goodbyeChannelId = goodbyeChannelId;
        updateDocument();
    }

    public void setLevelUpChannelId(String levelUpChannelId) {
        this.levelUpChannelId = levelUpChannelId;
        updateDocument();
    }

    public String getWelcomeChannelId() { return this.welcomeChannelId; }
    public String getGoodbyeChannelId() { return this.goodbyeChannelId; }
    public String getLevelUpChannelId() { return this.levelUpChannelId; }


    public BasicDBObject getSettingsDocument() {
        return new BasicDBObject()
                .append("guildId", this.guildId)
                .append("welcomeChannelId", this.welcomeChannelId)
                .append("goodbyeChannelId", this.goodbyeChannelId)
                .append("levelUpChannelId", this.levelUpChannelId);
    }
}

