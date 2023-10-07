package vestige.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vestige.Vestige;
import vestige.api.event.Listener;
import vestige.api.event.impl.ChatEvent;
import vestige.command.impl.*;

public class CommandManager {
	
	public List<Command> commands = new ArrayList<Command>();
	public String prefix = ".";
	
	public CommandManager() {
		Vestige.getInstance().getEventManager().register(this);
		
		commands.add(new Toggle());
		commands.add(new Bind());
		commands.add(new Say());
		commands.add(new Config());
		commands.add(new VClip());
		commands.add(new HClip());
	}

	@Listener
	public void handleChat(ChatEvent event) {
		String message = event.getMessage();
		
		if(!message.startsWith(prefix)) {
			return;
		}
		
		event.setCancelled(true);
		
		message = message.substring(prefix.length());
		
		boolean foundCommand = false;
		
		if(message.split(" ").length > 0) {
			String commandName = message.split(" ")[0];
			
			for(Command c : commands) {
				if(c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
					c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
					foundCommand = true;
					break;
				}
			}
		}
		
		if(!foundCommand) {
			Vestige.getInstance().addChatMessage("Error : Could not find command.");
		}
		
	}
	
}
