package command.commands;

import java.util.List;
import org.jetbrains.annotations.NotNull;

import com.mongodb.BasicDBObject;       // for database
import database.Database;
import database.dataClasses.RoleData;

import command.Command;         // for commands
import command.CommandSystem;
import command.Failure;
import exceptionWrappers.Parser;

import net.dv8tion.jda.api.entities.Guild;      // for integration with the server
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import widgets.SimpleEmbed;

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
    public Failure execute(@NotNull MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
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
        BasicDBObject roleQuery = new BasicDBObject();
        roleQuery.put("name", role.getName());
        roleQuery.put("id", role.getId());

//        System.out.println();


        String updateInfo = "";

        RoleData roleData = database.getRole(roleQuery);
        if (roleData == null) {
            roleData = new RoleData(
                    database,
                    role.getName(),
                    role.getId(),
                    priority
            );
        }

        // Handle addition
        if (priority > 0) {
            roleData.updateRole(priority);

            checkAndResolveConflict(database, priority, roleData);

            updateInfo = String.format("Successfully set %s to %d", args.get(0), priority);
        }

        // Handle removal
        if (priority <= 0) {
            roleData.deleteDocument();

            updateInfo = String.format("Successfully deleted role %s", args.get(0));
        }
        embed.setDescription(updateInfo);
        messageChannel.sendMessageEmbeds(embed.build()).queue();
        return null;
    }

    private void checkAndResolveConflict(@NotNull Database database, int priority, RoleData roleData) {
        BasicDBObject searchMap = new BasicDBObject().append("id", new BasicDBObject()
                .append("$ne", roleData.getId())); // Ensures that the query result won't be the object we've updated just now.

        searchMap.put("priority", priority);
        RoleData searchResult = database.getRole(searchMap);

        if (searchResult != null) {
            searchResult.updateRole(priority + 1);

            checkAndResolveConflict(database, priority + 1, searchResult);
        }
    }
}
