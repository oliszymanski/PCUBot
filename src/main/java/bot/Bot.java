package bot;

import command.CommandSystem;
import command.commands.*;
import database.Database;
import listeners.MessageListener;

import listeners.ReadyListener;
import listeners.UserJoinListener;
import listeners.UserLeaveListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {
    private static JDA jda;
    private static Database database;

    public static void main(String[] args) throws LoginException {
        if (args.length < 2) {
            System.out.println("Please ensure all arguments have been specified.");
            System.exit(1);
        }

        // Add new commands here
        database = new Database(args[1]);
        CommandSystem commandSystem = CommandSystem.createSystem()
                .addCommand(new ShowRolesCommand())
                .addCommand(new KickCommand())
                .addCommand(new WarnCommand())
                .addCommand(new HelpCommand())
                .addCommand(new SetRoleCommand())
                .addCommand(new BanCommand())
                .addCommand(new LevelCommand())
                .addCommand(new WarningsCommand())
                .addCommand(new SetWelcomeChannelCommand())
                .addCommand(new SetGoodbyeChannelCommand())
                .addCommand(new SetLevelUpChannelCommand());

        jda = JDABuilder.createLight(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS)       // main builder
                .addEventListeners(
                        new MessageListener(commandSystem),
                        new ReadyListener(),
                        new UserJoinListener(),
                        new UserLeaveListener())
                .setActivity(Activity.playing("Creating a bot"))
                .build();
    }

    public static JDA getJda() { return jda; }
    public static Database getDatabase() { return database; }
}