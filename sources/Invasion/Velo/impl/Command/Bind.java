package Velo.impl.Command;

import org.lwjgl.input.Keyboard;

import Velo.api.Command.Command;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;

public class Bind extends Command {
	
	public Bind() {
		super("Bind", "Binds a module by the name.", "bind <name> <key> | clear", "b");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length == 2) {
			String moduleName = args[0];
			String keyName = args[1];
			
			boolean foundModule = false;
			
			for(Module module : ModuleManager.modules) {
				if(module.name.equalsIgnoreCase(moduleName)) {
					module.key.setKeyCode(Keyboard.getKeyIndex(keyName.toUpperCase()));
					foundModule = true;
					ChatUtil.addChatMessage(String.format("Bound %s to %s", module.name, Keyboard.getKeyName(module.getKeybind()).toUpperCase()));
					break;
				}
			}
			
			ModuleManager.get("Killaura");
			
			if(!foundModule) {
				ChatUtil.addChatMessage("Module not found");
			}
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("clear")) {
				for(Module module : ModuleManager.modules) {
					
				}
			}
			
			ChatUtil.addChatMessage("bind <name> <key>");
		}
	}
}
