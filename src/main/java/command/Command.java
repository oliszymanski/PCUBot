package command;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public abstract class Command {
    public String title;
    public String name;
    public String description;
    public String usage;
    public int expectedArgs;
    public boolean requiresAdmin;


    public abstract Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) throws CloneNotSupportedException;
}
