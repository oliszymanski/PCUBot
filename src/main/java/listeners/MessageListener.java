package listeners;

import bot.Bot;
import command.CommandSystem;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import threads.LevelCooldownThread;

import java.util.HashMap;

public class MessageListener extends ListenerAdapter {

    private final CommandSystem commandSystem;

    public MessageListener(CommandSystem commandSystem) {
        this.commandSystem = commandSystem;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot()) {
            Message msg = event.getMessage();
            if (msg.getContentRaw().startsWith("!")) {
                // The prefix gets removed to get a "pure" query.
                String commandQuery = msg.getContentRaw().replaceFirst("!", "");

                commandSystem.executeCommand(event, commandQuery);
                return;
            }

            User author = event.getAuthor();
            String authorId = author.getId();
            HashMap<String, Integer> coolingDown = LevelCooldownThread.getCoolingDown();
            if (!coolingDown.containsKey(authorId)) {
                int EXP_REWARD = 12;
                Bot.getDatabase().getOrCreateUser(authorId).giveExp(EXP_REWARD, event.getGuild());
                System.out.printf("User %s has been awarded EXP.\n", authorId);
                coolingDown.put(authorId, 10);
            } else {
                System.out.printf("User %s is on a cooldown. They have %d seconds left \n", authorId, coolingDown.get(authorId));
            }
        }

    }
}
