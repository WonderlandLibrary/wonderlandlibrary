package epsilon.botting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import epsilon.botting.cmds.*;
public class Core {
	

	public List<BotCommand> commands = new ArrayList<BotCommand>();
	
	public Core() {
		setup();
	}
	
	private void print(String m) {
		MintAttackGUI.print(m);
	}
	
	public void setup() {
		
		commands.add(new Help());
		commands.add(new ConsoleIP());
		commands.add(new ConsolePort());
		commands.add(new ConsoleDelay());
		
	}

	public void handleChat(EventCMD event) {

		String message = event.getMessage();

		event.setCancelled();
		boolean foundCommand = false;
		
		if(message.split(" ").length > 0) {
			String commandName = message.split(" ")[0];
			for(BotCommand c :commands) {
				if(c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
					c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
					event.setCancelled();
					foundCommand = true;
					break;
				}
			}
		}
		
		if(!foundCommand) {
			MintAttackGUI.print("' "+ message + " ' isnt a command :/ Try 'help' instead! ");
		}
	
	}
	
	

}
