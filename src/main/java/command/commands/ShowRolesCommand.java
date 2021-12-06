package command.commands;

import command.Command;
import command.CommandSystem;
import command.Failure;
import database.Database;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import widgets.OneFieldWidget;
import widgets.SimpleEmbed;

import java.util.List;


public class ShowRolesCommand extends Command {

	public ShowRolesCommand() {		// defining full description of the command
		this.title = "Show Roles";
		this.name = "showrole";
		this.description = "shows a list of all available roles";
		this.usage = "takes no arguments";
		this.expectedArgs = 0;
		this.requiresAdmin = false;
	}

	@Override
	public Failure execute(MessageReceivedEvent msgEvent, CommandSystem commandSystem, List<String> args) {

		// creating a window which will show the whole collection
		SimpleEmbed embed = SimpleEmbed.createSimpleEmbed(this.title, "");


		// accessing collection in db
		Database database = CommandSystem.getDatabase();
		String userRoles = String.valueOf(database.getCollection("roles"));


		System.out.println(userRoles);		// just for tests only


		//this gives a compiler error uncomment it if you will work on it


		
		// getting them from the db			- yeah, small problem with this one


		if (args.size() == 0){
			// showing all elements
			public List<String> getRoles() {
				for (String i :) {
					OneFieldWidget widget = new OneFieldWidget(
							this.title,
							"All available roles",
							"Roles",
							""
					);
				}

				System.out.println("Hello there");

			}
		}


		return null;
	}
}
