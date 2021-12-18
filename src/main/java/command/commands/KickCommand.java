package command.commands;

import bot.Bot;
import command.Command;
import command.CommandSystem;
import command.Failure;
import exceptionWrappers.Getter;
import exceptionWrappers.Parser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.OneFieldWidget;

import java.util.List;

public class KickCommand extends Command {

    public KickCommand() {
        this.title = "Kick";
        this.name = "kick";
        this.description = "Kicks the user from the server";
        this.usage = "<userId> {<reason>}";
        this.expectedArgs = 1;
        this.requiresAdmin = true;
        this.requiresOwner = false;
    }

    @Override
    public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        String userIdRaw = args.get(0);
        String userId = args.get(0).replaceAll("[^0-9]", "");

        User user = Getter.getUser(userId);
        if (user == null) return new Failure("No such user found!");

        args.remove(0);

        String reason = (!args.isEmpty()) ? Parser.parseString(args) : "No reason given";

        Bot.getJda().openPrivateChannelById(userId).queue(privateChannel -> {
            OneFieldWidget widget = new OneFieldWidget(this.title, "You've been kicked from the server.", "Reason", reason);
            privateChannel.sendMessageEmbeds(widget.build()).queue();

            msgEvent.getGuild()
                    .kick(userId, reason)
                    .submit();
        });

        String successAlert = String.format("Succesfully kicked %s from the server", userIdRaw);

        OneFieldWidget widget = new OneFieldWidget(this.title, successAlert, "Reason", reason);
        msgEvent.getChannel().sendMessageEmbeds(widget.build()).complete();

        return null;
    }
}
