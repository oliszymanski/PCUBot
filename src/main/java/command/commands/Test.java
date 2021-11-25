package command.commands;

import command.Command;
import command.CommandSystem;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class Test extends Command {
    public Test() {
        this.name = "test";
        this.description = "Tests stuff";
        this.usage = "test";
        this.expectedArgs = 0;
    }

    @Override
    public void execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        MessageChannel messageChannel = msgEvent.getChannel();
        messageChannel.sendMessage("Test").complete();
    }
}
