import command.CommandSystem;
import command.commands.HelpCommand;
import command.commands.SetRoleCommand;
import command.commands.ShowRolesCommand;
import listeners.MessageListener;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {
    public static void main(String[] args) throws LoginException {
        if (args.length < 2) {
            System.out.println("Please ensure all arguments have been specified.");
            System.exit(1);
        }

        CommandSystem commandSystem = CommandSystem.createSystem(args[1])

                // commands that can be used by the bot
                .addCommand(new HelpCommand())
                .addCommand(new SetRoleCommand())
                .addCommand(new ShowRolesCommand());

        JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES)
                .addEventListeners(new MessageListener(commandSystem))
                .setActivity(Activity.playing("with your mom"))
                .build();
    }
}