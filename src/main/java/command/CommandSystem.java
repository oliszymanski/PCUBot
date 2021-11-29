package command;

import database.Database;
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
    private final Database database;

    public CommandSystem(String databaseAddress) {
        this.commandStorage = new HashMap<>();
        this.database = new Database(databaseAddress);
    }

    // This command only exists so that chaining functions can be available immediately
    public static CommandSystem createSystem(String databaseAddress) {
        return new CommandSystem(databaseAddress);
    }

    public HashMap<String, Command> getCommandStorage() { return this.commandStorage; }
    public Database getDatabase() { return this.database; }

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

        Command command = getCommandByName(potentialCommand);
        queryArray.remove(0);

        if (command == null) return;
        if (query.length() < command.expectedArgs) return;

        // Check for permission
        if (command.requiresAdmin) {
            Member member = msgEvent.getMember();
            if (member.hasPermission(Permission.ADMINISTRATOR)) {
                command.execute(msgEvent, this, queryArray);
                return;
            } // TODO: let the user know if they have no permission
            return;
        }

        // No need for an else block here, if the command requires admin and the user has no permission, it gets instantly interrupted.
        command.execute(msgEvent, this, queryArray);
    }
}
