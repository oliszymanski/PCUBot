package command.commands;

import bot.Bot;
import command.Command;
import command.CommandSystem;
import command.Failure;
import database.Database;
import database.dataClasses.RoleData;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.SimpleEmbed;

import java.util.ArrayList;
import java.util.List;


public class ShowRolesCommand extends Command {

	public ShowRolesCommand() {		// defining full description of the command
		this.title = "Show Roles";
		this.name = "showroles";
		this.description = "shows a list of all available roles";
		this.usage = " ";
		this.expectedArgs = 0;
		this.requiresAdmin = false;
		this.requiresOwner = false;
	}

	@Override
	public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {

		EmbedBuilder embedBuilder = new EmbedBuilder().setTitle(this.title);

		Database database = Bot.getDatabase();
		ArrayList<RoleData> roles = database.getAllRoles();

		if (!roles.isEmpty()) {
			for (RoleData roleData : roles) {
				String fieldString = String.format("Awarded at: %d", roleData.getRequiredLevel());
				embedBuilder.addField(roleData.getName(), fieldString, false);
			}
			msgEvent.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
			return null;
		}

		SimpleEmbed simpleEmbed = new SimpleEmbed(this.title, "There are no roles found.");
		msgEvent.getChannel().sendMessageEmbeds(simpleEmbed.build()).queue();
		return null;
	}
}
