package command.commands;

import command.Command;
import command.CommandSystem;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.SimpleEmbed;

import java.util.List;

public class TestCommand extends Command {
    public TestCommand() {
        this.name = "test";
        this.description = "Tests stuff";
        this.usage = "";
        this.expectedArgs = 0;
    }

    @Override
    public void execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        MessageEmbed embed = SimpleEmbed.createSimpleEmbed("Test", "Test").build();
        MessageChannel messageChannel = msgEvent.getChannel();
        messageChannel.sendMessageEmbeds(embed).queue();
    }
}
