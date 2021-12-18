package command.commands;

import bot.Bot;
import command.Command;
import command.CommandSystem;
import command.Failure;
import database.Database;
import database.dataClasses.SettingsData;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.SimpleEmbed;

import java.util.List;

public class SetWelcomeChannelCommand extends Command {
    public SetWelcomeChannelCommand() {
        this.title = "Set Welcome Channel";
        this.description = "Sets the welcome channel. Use \"none\" to remove.";
        this.name = "setwelcomechannel";
        this.usage = "<channelId>";
        this.expectedArgs = 1;
        this.requiresAdmin = false;
        this.requiresOwner = true;
    }

    @Override
    public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {
        String channelIdRaw = args.get(0);
        String channelId = args.get(0).replaceAll("[^0-9]", "");

        Database database = Bot.getDatabase();
        SettingsData settingsData = database.getOrCreateSettings(msgEvent.getGuild().getId());
        if (channelIdRaw.equalsIgnoreCase("none")) {
            settingsData.setGoodbyeChannelId(null);
            msgEvent.getChannel().sendMessageEmbeds(new SimpleEmbed(this.title, "Succesfully removed the welcome channel.").build()).queue();
            return null;
        }
        settingsData.setWelcomeChannelId(channelId);

        String info = String.format("Succesfully set welcome channel to %s", channelIdRaw);

        msgEvent.getChannel().sendMessageEmbeds(new SimpleEmbed(this.title, info).build()).queue();
        return null;
    }
}
