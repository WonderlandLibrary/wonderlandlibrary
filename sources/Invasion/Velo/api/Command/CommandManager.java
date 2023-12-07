package Velo.api.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Velo.api.Util.Other.ChatUtil;
import Velo.impl.Command.Bind;
import Velo.impl.Command.ClientName;
import Velo.impl.Command.ClientVersion;
import Velo.impl.Command.Config;
import Velo.impl.Command.Enchant;
import Velo.impl.Command.Friend;
import Velo.impl.Command.Toggle;
import Velo.impl.Event.EventChat;

public class CommandManager {
	
	public List<Command> commands = new ArrayList<Command>();
	public String prefix = ".";
	
	public CommandManager() {
		registerCommands();
	}
	
	public void registerCommands() {
		commands.add(new Toggle());
		commands.add(new Bind());
		commands.add(new Config());
		commands.add(new ClientName());
		commands.add(new ClientVersion());
		commands.add(new Friend());
	
	}
	
	public Command get(String cName) {
		for(Command c : this.commands) {
			if(c.commandName == cName) {
				return c;
			}
		}
		return null;
	}
	
	public void onChat(EventChat event) {
		String message = event.getMessage();
		
		if(!message.startsWith(prefix))
			return;
		
		event.setCancelled(true);
		
		message = message.substring(prefix.length());
		
		boolean foundCommand = false;
		
		if(message.split(" ").length > 0) {
			String commandName = message.split(" ")[0];
			
			for(Command c : commands) {
				if(c.othernames.contains(commandName) || c.commandName.equalsIgnoreCase(commandName)) {
					c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
					foundCommand = true;
					break;
				}
			}
		}
		
		if(!foundCommand) {
			ChatUtil.addChatMessage("Command not found.");
		}
	}
}
