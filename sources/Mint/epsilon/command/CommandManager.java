package epsilon.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import epsilon.Epsilon;
import epsilon.command.impl.*;
import epsilon.events.listeners.EventChat;

public class CommandManager {

	public List<Command> commands = new ArrayList<Command>();
	public String prefix = ".";
	
	public CommandManager() {
		setup();
	}
	
	public void setup() {
		commands.add(new Toggle());
		commands.add(new Bind());
		commands.add(new Info());
		commands.add(new Say());
		commands.add(new Vclip());
		commands.add(new Cracked());
		commands.add(new IP());
		commands.add(new Port());
		
	}
	public void handleChat(EventChat event) {
		String message = event.getMessage();
		
		if(!message.startsWith(prefix))
			return;
		
		event.setCancelled();
		
		message = message.substring(prefix.length());
		
		boolean foundCommand = false;
		
		if(message.split(" ").length > 0) {
			String commandName = message.split(" ")[0];
			for(Command c :commands) {
				if(c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
					c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
					foundCommand = true;
					break;
				}
			}
		}
		
		if(!foundCommand) {
			Epsilon.addChatMessage("Could not find command");
		}
	}	
}

