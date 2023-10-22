package epsilon.command.impl;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.command.Command;
import epsilon.modules.Module;


public class Info extends Command {

	public Info() {
		super("Info", "Displays info on a module", "info", "i");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			String moduleName = args[0];
			
			boolean foundModule = false;
			
			for(Module module : Epsilon.modules) {
				if(module.name.equalsIgnoreCase(moduleName)) {
					Epsilon.addChatMessage(module.name + ": " + module.description);
					
					foundModule = true;
					break;
				}
			}
			
			if(!foundModule) {
				Epsilon.addChatMessage("Could not find module.");
			}
		}
	}
	
}
