package command.commands;

import bot.Bot;
import command.Command;
import command.CommandSystem;
import command.Failure;
import command.Parser;
import database.Database;
import database.dataClasses.UserData;
import database.dataClasses.WarningData;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.OneFieldWidget;

import java.util.List;

public class WarnCommand extends Command {

    public WarnCommand() {
        this.title = "Warn";
        this.name = "warn";
        this.description = "Gives the user a warning";
        this.usage = "<userId> <reason>";
        this.requiresAdmin = true;
    }

    @Override
    public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        // TODO: Improve it later
        String userIdRaw = args.get(0);
        String userId = args.get(0).replaceAll("[^0-9]", "");

        User user = tryGetUser(userId);
        if (user == null) return new Failure("No such user found!");

        args.remove(0);

        Database database = CommandSystem.getDatabase();
        String reason = Parser.parseString(args);

        OneFieldWidget widget = new OneFieldWidget(this.title, String.format("Successfully warned %s", userIdRaw), "Reason", reason);
        UserData userData = database.getOrCreateUser(user.getId());
        WarningData warningData = WarningData.createNewWarning(database, reason);

        userData.addWarning(warningData);
        Bot.getJda().openPrivateChannelById(userId).queue(channel -> {
            OneFieldWidget privateWidget = new OneFieldWidget("Warning", "You have received a warning.", "Reason", reason);
            channel.sendMessageEmbeds(privateWidget.build()).queue();
        });

        msgEvent.getChannel().sendMessageEmbeds(widget.build()).queue();
        return null;
    }

    private User tryGetUser(String id) {
        try {
            return User.fromId(id);
        } catch(Exception e) {
            return null;
        }
    }
}
