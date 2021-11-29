package command.commands;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import command.Command;
import command.CommandSystem;
import command.Failure;
import command.Parser;
import database.Database;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.SimpleEmbed;

import java.util.List;

public class SetRoleCommand extends Command {
    public SetRoleCommand() {
        this.title = "Set Role";
        this.name = "setrole";
        this.description = "Gives the specified role a priority";
        this.usage = "<roleId> [priority]";
        this.expectedArgs = 2;
        this.requiresAdmin = true;
    }

    @Override
    public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        Guild guild = msgEvent.getGuild();
        String roleId = args.get(0).replaceAll("[^0-9]", "");
        Role role = guild.getRoleById(roleId);

        SimpleEmbed embed = SimpleEmbed.createSimpleEmbed(this.title, "");

        Integer priorityInteger = Parser.parseInt(args.get(1));
        if (priorityInteger == null) return new Failure("The priority must be a valid number!");

        int priority = priorityInteger;
        Database database = commandSystem.getDatabase();
        MessageChannel messageChannel = msgEvent.getChannel();

        // Basic role data
        BasicDBObject roleObject = new BasicDBObject();
        roleObject.put("name", role.getName());
        roleObject.put("id", role.getId());

        String updateInfo = "";

        // Handle addition
        if (priority > 0) {
            // We create a clone of the role's data so that we can update it along with priority.
            BasicDBObject roleUpdate = (BasicDBObject) roleObject.clone();
            roleUpdate.put("priority", priority);

            database.updateRole(roleObject, roleUpdate);
            BasicDBObject newRole = (BasicDBObject) database.getRole(roleUpdate);
            checkAndResolveConflict(database, priority, newRole);

            updateInfo = String.format("Succesfully set %s to %d", args.get(0), priority);
        }

        // Handle removal
        if (priority <= 0) {
            database.deleteRole(roleObject);
            updateInfo = String.format("Succesfully deleted role %s", args.get(0));
        }

        embed.setDescription(updateInfo);
        messageChannel.sendMessageEmbeds(embed.build()).queue();
        return null;
    }

    private void checkAndResolveConflict(Database database, int priority, DBObject roleObject) {
        BasicDBObject searchMap = new BasicDBObject().append("id", new BasicDBObject()
                .append("$ne", roleObject.get("id"))); // Ensures that the query result won't be the object we've updated just now.
        searchMap.put("priority", priority);
        BasicDBObject searchResult = (BasicDBObject) database.getRole(searchMap);

        if (searchResult != null) {
            BasicDBObject resultUpdate = (BasicDBObject) searchResult.clone();
            resultUpdate.put("priority", priority + 1);
            database.updateRole(searchResult, resultUpdate);

            checkAndResolveConflict(database, priority + 1, resultUpdate);
        }
    }
}
