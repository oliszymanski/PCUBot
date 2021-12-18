package listeners;

import bot.Bot;
import database.dataClasses.SettingsData;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import widgets.SimpleEmbed;

public class UserLeaveListener extends ListenerAdapter {
    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        SettingsData settingsData = Bot.getDatabase().getOrCreateSettings(event.getGuild().getId());
        String goodbyeChannelId = settingsData.getGoodbyeChannelId();
        if (goodbyeChannelId != null && !goodbyeChannelId.equals("")) {
            TextChannel goodbyeChannel = event.getGuild().getTextChannelById(goodbyeChannelId);
            if (goodbyeChannel == null) return;

            String memberId = String.format("<@%s>", event.getUser().getId());
            String welcomeText = String.format("%s has left the server.", memberId);
            SimpleEmbed simpleEmbed = new SimpleEmbed("Goodbye", welcomeText);

            goodbyeChannel.sendMessageEmbeds(simpleEmbed.build()).queue();
        }
    }
}
