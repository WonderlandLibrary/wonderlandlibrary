package xyz.cucumber.base.commands;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import xyz.cucumber.base.events.ext.EventChat;

import xyz.cucumber.base.commands.cmds.*;



public class CommandManager {
	
	public Set<Command> commands = new HashSet();
	
	private String prefix = "§7[§cGothaj§7] §r";
	
	private String cmdprefix = ".";
	
	public CommandManager() {
		commands.add(new BindCommand());
		commands.add(new ConfigCommand());
		commands.add(new FriendsCommand());
		commands.add(new ToggleCommand());
	}
	
	public void commandHandler(EventChat e) {
		
		if(!e.getMessage().startsWith(cmdprefix)) return;
		
		List<String> message = new ArrayList();
		
		message.addAll(Arrays.asList(e.getMessage().toLowerCase().substring(1).split(" ")));
		
		e.setCancelled(true);
		
		String command = message.get(0);
		
		for(Command cmd : commands) {
			for(String alias : cmd.getAliases()) {
				if(!alias.toLowerCase().equals(command)) continue;
				message.remove(0);
				
				
				cmd.onSendCommand(message.toArray(new String[0]));
				return;
			}
		}
		sendChatMessage("§cCommand not found! Try .help for help.");
	}
	
	public void sendChatMessage(String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(prefix + message));
	}
	
}
