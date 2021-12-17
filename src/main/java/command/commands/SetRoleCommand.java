package command.commands;

import bot.Bot;
import command.Command;
import command.CommandSystem;
import command.Failure;
import database.Database;
import database.dataClasses.RoleData;
import exceptionWrappers.Parser;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import widgets.SimpleEmbed;

import java.util.List;

public class SetRoleCommand extends Command {
    private final static int MAXIMUM_LEVEL = 999;

    public SetRoleCommand() {
        this.title = "Set Role";
        this.name = "setrole";
        this.description = "Gives the specified role a priority";
        this.usage = "<roleId> [level]";
        this.expectedArgs = 2;
        this.requiresAdmin = true;
    }

    @Override
    public Failure execute(@NotNull MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        Guild guild = msgEvent.getGuild();
        String roleId = args.get(0).replaceAll("[^0-9]", "");
        Role role = guild.getRoleById(roleId);

        SimpleEmbed embed = SimpleEmbed.createSimpleEmbed(this.title, "");

        Integer levelInteger = Parser.parseInt(args.get(1));
        if (levelInteger == null) return new Failure("The priority must be a valid number!");

        int level = levelInteger;
        Database database = Bot.getDatabase();
        MessageChannel messageChannel = msgEvent.getChannel();

        String updateInfo = "";
        RoleData roleData = database.getOrCreateRole(role.getId(), role.getName());


        // Handle addition
        if (level > 0) {
            if (level > MAXIMUM_LEVEL) return new Failure(String.format("Please set a level below the maximum level %d", MAXIMUM_LEVEL));
            if (database.getRole(level) != null) return new Failure("A role with this level already exists!");
            roleData.setLevel(level);

            updateInfo = String.format("Successfully set %s to %d", args.get(0), level);
        }

        // Handle removal
        if (level <= 0) {
            roleData.deleteDocument();
            updateInfo = String.format("Successfully deleted role %s", args.get(0));
        }

        embed.setDescription(updateInfo);
        messageChannel.sendMessageEmbeds(embed.build()).queue();
        return null;
    }
}
