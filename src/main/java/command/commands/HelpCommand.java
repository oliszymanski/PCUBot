package command.commands;

import command.Command;
import command.CommandSystem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class HelpCommand extends Command {
    private EmbedBuilder embedBuilder;

    public HelpCommand() {
        this.name = "help";
        this.description = "shows this message";
        this.usage = "{<command>}";
        this.expectedArgs = 0;
    }

    @Override
    public void execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        this.embedBuilder = new EmbedBuilder();
        this.embedBuilder.setTitle("Help");

        if (args.size() > 0) {
            getCommandHelp(args.get(0), commandSystem);
        } else {
            for (String name : commandSystem.getCommandStorage().keySet()) {
                getCommandHelp(name, commandSystem);
            }
        }

        MessageChannel messageChannel = msgEvent.getChannel();
        messageChannel.sendMessageEmbeds(this.embedBuilder.build()).queue();
    }

    private void getCommandHelp(String commandName, CommandSystem commandSystem) {
        Command command = commandSystem.getCommandByName(commandName);
        if (command == null) return;

        String titleString = String.format("%s: %s", command.name, command.description);
        String usageString = String.format("!%s: %s", command.name, command.usage);

        this.embedBuilder.addField(titleString, usageString, false);

    }

}
