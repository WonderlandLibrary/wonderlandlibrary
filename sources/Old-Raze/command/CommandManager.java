package markgg.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import markgg.RazeClient;
import markgg.command.impl.SetYaw;
import markgg.command.impl.Bind;
import markgg.command.impl.Config;
import markgg.command.impl.Discord;
import markgg.command.impl.Help;
import markgg.command.impl.IGN;
import markgg.command.impl.Ip;
import markgg.command.impl.Prefix;
import markgg.command.impl.Say;
import markgg.command.impl.SetPitch;
import markgg.command.impl.Toggle;
import markgg.command.impl.VClip;
import markgg.command.impl.Watermark;
import markgg.event.impl.ChatEvent;

public class CommandManager {

	public List<Command> commands = new ArrayList<Command>();
	public String prefix;

	public CommandManager() {
		File configFile = new File("Raze/config.properties");
	    if (!configFile.exists()) {
	        Properties properties = new Properties();
	        properties.setProperty("prefix", ".");
	        try {
	            OutputStream outputStream = new FileOutputStream(configFile);
	            properties.store(outputStream, "Raze Properties");
	            outputStream.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    Properties properties = new Properties();
	    try {
	        FileInputStream inputStream = new FileInputStream(configFile);
	        properties.load(inputStream);
	        inputStream.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    prefix = properties.getProperty("prefix", ".");
		setup();
	}
	
	public void setup() {
		commands.add(new Watermark());
		commands.add(new Prefix());
		commands.add(new Help());
		commands.add(new Discord());
		commands.add(new Toggle());
		commands.add(new Config());
		commands.add(new Bind());
		commands.add(new Say());
		commands.add(new IGN());
		commands.add(new Ip());
		commands.add(new VClip());
		commands.add(new SetYaw());
		commands.add(new SetPitch());
	}

	public void handlechat(ChatEvent event) {
		String message = event.getMessage();
		
		if(!message.startsWith(prefix))
			return;
		
		event.setCancelled(true);
		
		message = message.substring(prefix.length());
		
		boolean foundCommand = false;

		if(message.split(" ").length > 0) {
			String commandName = message.split(" ")[0];
			
			for (Command c : this.commands) {
				if (c.aliases.contains(commandName) || c.name.equalsIgnoreCase(commandName)) {
					c.onCommand(Arrays.copyOfRange(message.split(" "), 1, message.split(" ").length), message);
					foundCommand = true;
					break;	
				}
			}
		}
		if(!foundCommand) {
			RazeClient.addChatMessage("Could not find command.");
		}
	}

}