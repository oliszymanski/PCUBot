package command.commands;

import command.Command;
import command.CommandSystem;
import command.Parser;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class RepeatCommand extends Command {
    public RepeatCommand() {
        this.name = "repeat";
        this.description = "repeats a message a specified amount of times";
        this.usage = "[amount] <text>";
        this.expectedArgs = 2;
    }

    @Override
    public void execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        Integer amountWrapped = Parser.parseInt(args.get(0));
        if (amountWrapped == null) return;

        MessageChannel messageChannel = msgEvent.getChannel();

        args.remove(0);

        int amount = amountWrapped;

        String baseText = args.toString();
        StringBuilder text = new StringBuilder();

        for (int i=0; i < amount; i++) {
            text.append(baseText).append(" ");
        }

        messageChannel.sendMessage(text.toString()).queue();

    }
}
