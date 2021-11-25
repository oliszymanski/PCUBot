import command.CommandSystem;
import command.commands.HelpCommand;
import command.commands.RepeatCommand;
import command.commands.TestCommand;
import listeners.MessageListener;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {
    public static void main(String[] args) throws LoginException {
        CommandSystem commandSystem = CommandSystem.createSystem()
                .addCommand(new HelpCommand())
                .addCommand(new TestCommand())
                .addCommand(new RepeatCommand());

        if (args.length < 1) {
            System.out.println("Please specify a token");
            System.exit(1);
        }

        JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new MessageListener(commandSystem))
                .setActivity(Activity.playing("with your mom"))
                .build();
    }
}