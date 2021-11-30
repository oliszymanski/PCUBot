package command.commands;

import command.Command;
import command.CommandSystem;
import command.Failure;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import widgets.SimpleEmbed;		// for discord widget
import database.Database;		// for accessing db


import java.util.List;


public class ShowRoleCommand extends Command {

	public ShowRoleCommand() {		// defining full description of the command
		this.title = "Show Role";
		this.name = "showrole";
		this.description = "shows a list of all available roles";
		this.usage = "";
		this.expectedArgs = 0;
		this.requiresAdmin = false;
	}

	@Override
	public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {

		// creating a window which will show the whole collection
		SimpleEmbed embed = SimpleEmbed.createSimpleEmbed(this.title, "");



		// showing all elements


		// getting them from the db


		// getting permission to access the db

		return null;
	}
}
