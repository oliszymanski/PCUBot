package listeners;

import bot.Bot;
import database.dataClasses.SettingsData;
import database.dataClasses.UserData;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import widgets.SimpleEmbed;

public class UserJoinListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        SettingsData settingsData = Bot.getDatabase().getOrCreateSettings(event.getGuild().getId());
        String welcomeChannelId = settingsData.getWelcomeChannelId();
        if (welcomeChannelId != null && !welcomeChannelId.equals("")) {
            TextChannel welcomeChannel = event.getGuild().getTextChannelById(welcomeChannelId);
            if (welcomeChannel != null) {
                String memberId = String.format("<@%s>", event.getMember().getId());
                String welcomeText = String.format("%s has joined the server, welcome!", memberId);
                SimpleEmbed simpleEmbed = new SimpleEmbed("Welcome", welcomeText);

                welcomeChannel.sendMessageEmbeds(simpleEmbed.build()).queue();
            }
        }
        // Update the user upon joining the server
        UserData userData = Bot.getDatabase().getOrCreateUser(event.getMember().getId());
        userData.setLevel(userData.getLevel(), event.getGuild());
    }
}
