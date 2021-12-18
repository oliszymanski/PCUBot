package command.commands;

import bot.Bot;
import command.Command;
import command.CommandSystem;
import command.Failure;
import database.Database;
import database.dataClasses.UserData;
import exceptionWrappers.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class LevelCommand extends Command {
    public LevelCommand() {
        this.title = "Level";
        this.name = "level";
        this.description = "shows an user's level";
        this.usage = "{<userId>}";
        this.expectedArgs = 0;
        this.requiresAdmin = false;
        this.requiresOwner = false;
    }


    @Override
    public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        User user = msgEvent.getAuthor();

        String userIdRaw = Getter.get(args, 0);
        if (userIdRaw != null) {
            String userId = args.get(0).replaceAll("[^0-9]", "");
            user = Getter.getUser(userId);
        } else {
            userIdRaw = String.format("<@%s>", user.getId());
        }

        if (args.size() != 0) {
            args.remove(0);
        }

        Database database = Bot.getDatabase();
        UserData userData = database.getOrCreateUser(user.getId());

        String description = String.format("Level stats for: %s", userIdRaw);
        int currentExp = userData.getCurrentExp();
        int expUntilNext = userData.getExpUntilNext();

        double levelPercentage = ((double)currentExp / (double)expUntilNext) * 100;
        String progressText = String.format("%s/%s (%.2f%%)", currentExp, expUntilNext, levelPercentage);
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle(this.title)
                .setDescription(description)
                .addField("Current Level", String.format("%d", userData.getLevel()), true)
                .addField("Progress until next Level", progressText, true);

        msgEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();


        return null;
    }


}
