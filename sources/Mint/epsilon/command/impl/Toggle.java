package epsilon.command.impl;

import epsilon.Epsilon;
import epsilon.command.Command;
import epsilon.modules.Module;


public class Toggle extends Command {

	public Toggle() {
		super("Toggle", "Toggles a module by name.", "toggle <name>", "t");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			String moduleName = args[0];
			
			boolean foundModule = false;
			
			for(Module module : Epsilon.modules) {
				if(module.name.equalsIgnoreCase(moduleName)) {
					module.toggle();
					
					Epsilon.addChatMessage((module.isEnabled() ? "Enabled" : "Disabled") + " " + module.name);
					
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
