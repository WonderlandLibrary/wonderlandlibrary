package markgg.command.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import markgg.RazeClient;
import markgg.command.Command;
import markgg.config.ConfigSystem;
import markgg.modules.Module;
import net.minecraft.util.EnumChatFormatting;

public class Config extends Command {

	public Config() {
		super("Config", "Saves or loads configs", "config load <name> | config save | config list", "cfg");
	}

	@Override
	public void onCommand(String[] args, String command) {
		String filename;
		if (args.length > 1 && !args[1].isEmpty()) {
			filename = args[1];
			if (!filename.endsWith(".json")) {
				filename += ".json";
			}
		} else {
			filename = "default.json";
		}
		
		File folder = new File("Raze/configs");
		File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

		try {
			if(args[0].equalsIgnoreCase("save")) {
				RazeClient.getCfgsystem().saveConfig(filename);
				RazeClient.addChatMessage("Saved config to " + EnumChatFormatting.GREEN + filename);
			} else if(args[0].equalsIgnoreCase("load")) {
				boolean fileExists = false;
				for (File file : files) {
			        if (file.getName().equals(filename)) {
			            fileExists = true;
			            RazeClient.getCfgsystem().loadConfig(file.getAbsolutePath());
			            if (RazeClient.getCfgsystem().error != "no error") {
			                RazeClient.addChatMessage("Error loading config: " + EnumChatFormatting.RED + "incompatible version");
			                return;
			            }
			            RazeClient.addChatMessage("Successfully loaded config " + EnumChatFormatting.GREEN + filename);
			            break;
			        }
			    }
				if (!fileExists) {
			        RazeClient.addChatMessage("Error loading config: " + EnumChatFormatting.RED + filename + " not found");
			    }
			} else if(args[0].equalsIgnoreCase("list")) {
				if (files != null && files.length > 0) {
				    StringBuilder messageBuilder = new StringBuilder("Available configs: " + EnumChatFormatting.GREEN);

				    for (File file : files) {
				        String fileName = file.getName();
				        fileName = fileName.substring(0, fileName.lastIndexOf('.'));
				        messageBuilder.append(fileName).append(", ");
				    }

				    String message = messageBuilder.toString().trim();
				    if (message.endsWith(",")) {
				        message = message.substring(0, message.length() - 1);
				    }

				    RazeClient.addChatMessage(message);
				} else {
					RazeClient.addChatMessage("No configs found.");
				}
			} else
				RazeClient.addChatMessage("Error: " + EnumChatFormatting.RED + "config paramater cannot be empty");
		} catch (Exception e) {
			RazeClient.addChatMessage("Error getting config: " + EnumChatFormatting.RED + e.getMessage());
		    e.printStackTrace();
		}
	}

}
