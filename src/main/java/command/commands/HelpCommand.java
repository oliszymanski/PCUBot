package command.commands;

import command.Command;
import command.CommandSystem;
import command.Failure;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class HelpCommand extends Command {
    private EmbedBuilder embedBuilder;

    public HelpCommand() {
        this.title = "Help";
        this.name = "help";
        this.description = "shows this message";
        this.usage = "{<command>}";
        this.expectedArgs = 0;
        this.requiresAdmin = false;
    }

    @Override
    public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        this.embedBuilder = new EmbedBuilder();
        this.embedBuilder.setTitle(this.title);

        if (args.size() > 0) {
            CommandSystem.getCommandHelp(args.get(0), commandSystem, this.embedBuilder);
        } else {
            for (String name : commandSystem.getCommandStorage().keySet()) {
                CommandSystem.getCommandHelp(name, commandSystem, this.embedBuilder);
            }
        }

        MessageChannel messageChannel = msgEvent.getChannel();
        messageChannel.sendMessageEmbeds(this.embedBuilder.build()).queue();
        return null;
    }

}
