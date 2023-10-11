package artemis.command.commands;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;
import artemis.file.Configer;
import net.minecraft.client.Minecraft;

public class Config extends Command{

	@Override
	public String getAlias() {
		return "config";
	}

	@Override
	public String getDescription() {
		return "Allows user to load configs.";
	}

	@Override
	public String getSyntax() {
		return ".config save [Name] | .config load [Name] | .config remove [Name]";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		System.out.println(Minecraft.getMinecraft().mcDataDir);
		System.out.println(args[0]);
		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("save")) {
				Configer.saveConfig(args[1]);
			} else if(args[0].equalsIgnoreCase("load")) {
				
				Configer.loadConfig(args[1]);
			} else if(args[0].equalsIgnoreCase("remove")) {
				Configer.configM.remove(args[1]);
			} else {
				Artemis.addChatMessage(".config <save/load/remove> <Config>");
			}
		} else {
			Artemis.addChatMessage(".config <save/load/remove> <Config>");
		}
	}

}
