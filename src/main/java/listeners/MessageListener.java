package listeners;

import command.CommandSystem;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private final CommandSystem commandSystem;

    public MessageListener(CommandSystem commandSystem) {
        this.commandSystem = commandSystem;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message msg = event.getMessage();
        if (msg.getContentRaw().startsWith("!")) {
            // The prefix gets removed to get a "pure" query.
            String commandQuery = msg.getContentRaw().replaceFirst("!", "");

            // Java for gods sake why the fuck are you forcing me do to that?
            try {
                commandSystem.executeCommand(event, commandQuery);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }
}
