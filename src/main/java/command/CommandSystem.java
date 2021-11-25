package command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.*;

public class CommandSystem {
    private final HashMap<String, Command> commandStorage;

    public CommandSystem() {
        this.commandStorage = new HashMap<>();
    }

    public Command getCommandByName(String name) {
        return this.commandStorage.get(name);
    }

    public void addCommand(Command command) {
        this.commandStorage.put(command.name, command);
    }

    public void executeCommand(MessageReceivedEvent msgEvent, String query) {
        List<String> queryArray = new ArrayList<>(Arrays.asList(query.split(" ")));
        String potentialCommand = queryArray.get(0);

        Command command = getCommandByName(potentialCommand);
        queryArray.remove(0);

        if (command == null) return;
        if (query.length() < command.expectedArgs) return;

        command.execute(msgEvent, this, queryArray);
    }
}
