package command;

import database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandSystem {
    private final HashMap<String, Command> commandStorage;
    private static Database database;

    public CommandSystem(String databaseAddress) {
        this.commandStorage = new HashMap<>();
        database = new Database(databaseAddress);

    }

    // This command only exists so that chaining functions can be available immediately
    public static CommandSystem createSystem(String databaseAddress) {
        return new CommandSystem(databaseAddress);
    }

    public HashMap<String, Command> getCommandStorage() { return this.commandStorage; }
    public static Database getDatabase() { return database; }

    public Command getCommandByName(String name) {
        return this.commandStorage.get(name);
    }

    public CommandSystem addCommand(Command command) {
        this.commandStorage.put(command.name, command);
        return this;
    }

    public void executeCommand(@NotNull MessageReceivedEvent msgEvent, String query) {
        List<String> queryArray = new ArrayList<>(Arrays.asList(query.split(" ")));
        String potentialCommand = queryArray.get(0);

        EmbedBuilder embedBuilder = new EmbedBuilder();

        Command command = getCommandByName(potentialCommand);
        queryArray.remove(0);

        if (command == null) return;
        Failure failure = (queryArray.size() < command.expectedArgs) ? Failure.createFailure("Not enough arguments") : null;

        if (failure == null) {
            // Check for permission
            if (command.requiresAdmin) {
                Member member = msgEvent.getMember();
                if (member.hasPermission(Permission.ADMINISTRATOR)) {
                    failure = command.execute(msgEvent, this, queryArray);
                }

                else failure = new Failure("You don't have permission to do that.");
            }
            else {
                failure = command.execute(msgEvent, this, queryArray);
            }
        }

        if (failure != null) {
            String failureFormat = String.format("Error: %s", failure.getReason());
            embedBuilder
                    .setTitle(command.title)
                    .setDescription(failureFormat);

            getCommandHelp(command.name, this, embedBuilder);

            msgEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
        }
    }

    public static void getCommandHelp(String commandName, CommandSystem commandSystem, EmbedBuilder embedBuilder) {
        Command command = commandSystem.getCommandByName(commandName);
        if (command == null) return;

        String titleString = String.format("%s: %s", command.name, command.description);
        String usageString = String.format("!%s: %s", command.name, command.usage);

        embedBuilder.addField(titleString, usageString, false);

    }
}
