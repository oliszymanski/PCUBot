package command.commands;

import bot.Bot;
import command.Command;
import command.CommandSystem;
import command.Failure;
import database.Database;
import database.dataClasses.UserData;
import exceptionWrappers.Getter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.OneFieldWidget;

import java.util.List;

public class LevelCommand extends Command {
    public LevelCommand() {
        this.title = "Level";
        this.name = "level";
        this.description = "shows an user's level";
        this.usage = "{<userId>}";
        this.expectedArgs = 0;
        this.requiresAdmin = false;
    }


    @Override
    public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        User user = msgEvent.getAuthor();

        String userIdRaw = Getter.get(args, 0);
        if (userIdRaw != null) {
            String userId = args.get(0).replaceAll("[^0-9]", "");
            user = Getter.getUser(userId);
        }

        if (args.size() != 0) {
            args.remove(0);
        }

        User finalUser = user;

        Bot.getJda().retrieveUserById(user.getId()).map(User::getName).queue(userName -> {
            String title = String.format("Level: %s", userName);

            Database database = CommandSystem.getDatabase();
            UserData userData = database.getOrCreateUser(finalUser.getId());

            String description = String.format("Current Level: %d", userData.getLevel());
            int currentExp = userData.getCurrentExp();
            int expUntilNext = userData.getExpUntilNext();

            double levelPercentage = ((double)currentExp / (double)expUntilNext) * 100;
            String progressText = String.format("%s/%s (%.2f%%)", currentExp, expUntilNext, levelPercentage);

            OneFieldWidget oneFieldWidget = new OneFieldWidget(title, description, "Progress until next", progressText);
            msgEvent.getChannel().sendMessageEmbeds(oneFieldWidget.build()).queue();
        });

        return null;
    }


}
