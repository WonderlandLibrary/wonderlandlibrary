package markgg.command.impl;

import org.lwjgl.input.Keyboard;

import markgg.RazeClient;
import markgg.command.Command;
import markgg.config.KeybindSystem;
import markgg.modules.Module;
import net.minecraft.util.EnumChatFormatting;

public class Bind extends Command {

	public Bind() {
		super("Bind", "Binds a module to a key.", "bind <module> <key> | bind remove <module> | bind clear", "b");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		//.bind remove <module>
		
		if(args.length == 2 && !args[0].equalsIgnoreCase("remove") || !args[0].equalsIgnoreCase("clear")) {
			String moduleName = args[0];
			String keyName = args[1];
			boolean foundModule = false;
			for (Module module : RazeClient.getModuleManager().getModules().values()) {
				if (module.name.equalsIgnoreCase(moduleName)) {
					module.keyCode = Keyboard.getKeyIndex(keyName.toUpperCase());
					RazeClient.addChatMessage("Bound " + EnumChatFormatting.GREEN + module.name + EnumChatFormatting.WHITE + " to " + EnumChatFormatting.GREEN + Keyboard.getKeyName(module.getKey()));
					KeybindSystem.saveKeybinds("keybinds.json");
					foundModule = true;
					break;
				} 
			} 
			if (!foundModule)
				RazeClient.addChatMessage("Error binding module: " + EnumChatFormatting.RED + "could not find module.");
		} else if(args[0].equalsIgnoreCase("remove")) {
			String moduleName = args[1];
			boolean foundModule = false;
			for (Module module : RazeClient.getModuleManager().getModules().values()) {
				if (module.name.equalsIgnoreCase(moduleName)) {
					module.keyCode = 0;
					RazeClient.addChatMessage("Removed the bind from " + EnumChatFormatting.GREEN + module.name);
					KeybindSystem.saveKeybinds("keybinds.json");
					foundModule = true;
					break;
				} 
			} 
			if (!foundModule)
				RazeClient.addChatMessage("Error binding module: " + EnumChatFormatting.RED + "could not find module.");
		}else if(args[0].equalsIgnoreCase("clear")) {
			for (Module module : RazeClient.getModuleManager().getModules().values()) {
				module.keyCode = 0;
				KeybindSystem.saveKeybinds("keybinds.json");
			}
			RazeClient.addChatMessage("Cleared all binds.");
		} else 
			RazeClient.addChatMessage("Error:" + EnumChatFormatting.RED + "could not find argument");
	}

}
