package command.commands;

import command.Command;
import command.CommandSystem;
import command.Failure;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import widgets.SimpleEmbed;		// for discord widget

import java.util.List;


public class ShowRolesCommand extends Command {

	public ShowRolesCommand() {		// defining full description of the command
		this.title = "Show Roles";
		this.name = "showrole";
		this.description = "shows a list of all available roles";
		this.usage = " ";
		this.expectedArgs = 0;
		this.requiresAdmin = false;
	}

	@Override
	public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {

		// creating a window which will show the whole collection
		SimpleEmbed embed = SimpleEmbed.createSimpleEmbed(this.title, "");


		// accessing collection in db
		System.out.println(CommandSystem.getDatabase().getCollection("roles"));


		// showing all elements
		System.out.println("Your only job is to show the roles!");

		// getting them from the db



		return null;
	}
}
