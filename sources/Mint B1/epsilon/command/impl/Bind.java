package epsilon.command.impl;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.command.Command;
import epsilon.modules.Module;


public class Bind extends Command {

	public Bind() {
		super("Bind", "Binds a module by name", "bind <name> <key> | clear", "b");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length == 2) {
			String moduleName = args[0];
			String keyName = args[1];
			
			boolean foundModule = false;
			
			for(Module module : Epsilon.modules) {
				if(module.name.equalsIgnoreCase(moduleName)) {
					module.keyCode.setKeycode(Keyboard.getKeyIndex(keyName.toUpperCase()));
					
					Epsilon.addChatMessage(String.format("Bound %s to %s", module.name, Keyboard.getKeyName(module.getKey())));
					foundModule = true;
					break;
				}
			}
			
			if(!foundModule) {
				Epsilon.addChatMessage("Could not find module");
			}
			
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("clear")) {
				for(Module module : Epsilon.modules) {
					module.keyCode.setKeycode(Keyboard.KEY_NONE);
				}
			}
			Epsilon.addChatMessage("Cleared all binds.");
		}
	}
	
}
