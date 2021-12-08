package command.commands;

import bot.Bot;
import command.Command;
import command.CommandSystem;
import command.Failure;
import database.Database;
import exceptionWrappers.Getter;
import exceptionWrappers.Parser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.OneFieldWidget;
import widgets.SimpleEmbed;

import java.util.List;

public class BanCommand extends Command {
    public BanCommand() {
        this.title = "Ban";
        this.name = "ban";
        this.description = "Permanently bans the user";
        this.usage = "<userId> {<reason>}";
        this.expectedArgs = 1;
        this.requiresAdmin = true;
    }

    @Override
    public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        String userIdRaw = args.get(0);
        String userId = args.get(0).replaceAll("[^0-9]", "");

        User user = Getter.getUser(userId);
        if (user == null) return new Failure("No such user found!");

        args.remove(0);

        Database database = CommandSystem.getDatabase();
        String reason = (!args.isEmpty()) ? Parser.parseString(args) : null;

        Bot.getJda().openPrivateChannelById(userId).queue(privateChannel -> {
            if (reason != null) {
                OneFieldWidget widget = new OneFieldWidget(this.title, "You've been permanently banned from the server.", "Reason", reason);
                privateChannel.sendMessageEmbeds(widget.build()).queue();
            } else {
                SimpleEmbed widget = new SimpleEmbed(this.title, "You've been permanently banned from the server.");
                privateChannel.sendMessageEmbeds(widget.build()).queue();
            }

            msgEvent.getGuild()
                    .ban(userId, 0, reason)
                    .submit();
        });

        database.getUser(userId).deleteDocument();

        String successAlert = String.format("Succesfully banned %s from the server", userIdRaw);
        if (reason != null) {
            OneFieldWidget widget = new OneFieldWidget(this.title, successAlert, "Reason", reason);
            msgEvent.getChannel().sendMessageEmbeds(widget.build()).queue();
            return null;
        }

        SimpleEmbed widget = new SimpleEmbed(this.title, successAlert);
        msgEvent.getChannel().sendMessageEmbeds(widget.build()).complete();

        return null;
    }
}
