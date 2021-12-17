package command.commands;

import command.Command;
import command.CommandSystem;
import command.Failure;
import database.Database;
import database.dataClasses.UserData;
import database.dataClasses.WarningData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.OneFieldWidget;

import java.util.ArrayList;
import java.util.List;

public class WarningsCommand extends Command {
    public WarningsCommand() {
        this.title = "Warnings";
        this.name = "warnings";
        this.description = "shows a list of all warnings the specified user received";
        this.usage = "<userId>";
        this.expectedArgs = 1;
        this.requiresAdmin = true;
    }

    @Override
    public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        String userIdRaw = args.get(0);
        String userId = args.get(0).replaceAll("[^0-9]", "");

        Database database = CommandSystem.getDatabase();
        UserData userData = database.getUser(userId);
        ArrayList<WarningData> warnings = userData.getAllWarnings();

        String description = String.format("Warnings for %s", userIdRaw);

        EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(this.title).setDescription(description);

        if (!warnings.isEmpty()) {
            for (WarningData warningData : warnings) {
                String fieldTitle = String.format("Date: %s", warningData.getDateFormatted());
                String fieldText = String.format("Reason: %s", warningData.getReason());
                embedBuilder.addField(fieldTitle, fieldText, false);
            }
            msgEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
            return null;
        }

        OneFieldWidget oneFieldWidget = new OneFieldWidget(this.title, description, "This user has no warnings", "Wow");
        msgEvent.getChannel().sendMessageEmbeds(oneFieldWidget.build()).queue();
        return null;
    }
}
