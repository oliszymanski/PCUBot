package command.commands;

import command.Command;
import command.CommandSystem;
import command.Failure;
import command.Parser;
import database.Database;
import database.dataClasses.UserData;
import database.dataClasses.WarningData;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.SimpleEmbed;

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
        String userIdRaw = args.get(0);
        String userId = args.get(0).replaceAll("[^0-9]", "");
        User user = User.fromId(userId);
        if (user == null) return new Failure("No such user exists!");

        args.remove(0);

        Database database = commandSystem.getDatabase();

        SimpleEmbed simpleEmbed = new SimpleEmbed(this.title, String.format("Successfully warned %s", userIdRaw));
        UserData userData = database.getOrCreateUser(user.getId());
        WarningData warningData = WarningData.createNewWarning(database, Parser.parseString(args));

        userData.addWarning(warningData);

        msgEvent.getChannel().sendMessageEmbeds(simpleEmbed.build()).queue();
        return null;
    }
}
