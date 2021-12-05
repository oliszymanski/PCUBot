package bot;

import command.CommandSystem;
import command.commands.HelpCommand;
import command.commands.SetRoleCommand;
import command.commands.ShowRolesCommand;
import command.commands.WarnCommand;
import listeners.MessageListener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class Bot {
    private static JDA jda;

    public static void main(String @NotNull [] args) throws LoginException {
        if (args.length < 2) {
            System.out.println("Please ensure all arguments have been specified.");
            System.exit(1);
        }

        CommandSystem commandSystem = CommandSystem.createSystem(args[1])

                // bot commands that can be used by authorised users
                .addCommand(new ShowRolesCommand())
                .addCommand(new WarnCommand())
                .addCommand(new HelpCommand())
                .addCommand(new SetRoleCommand());


        jda = JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES)       // main builder
                .addEventListeners(new MessageListener(commandSystem))
                .setActivity(Activity.playing("Creating a bot"))
                .build();
    }

    public static JDA getJda() { return jda; }
}