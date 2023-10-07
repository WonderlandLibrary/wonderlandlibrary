package vestige.command.impl;

import vestige.Vestige;
import vestige.api.module.Module;
import vestige.command.Command;

public class Toggle extends Command {
	
	public Toggle() {
		super("Toggle", "Toggles a module by name", "toggle <name>", "t");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			String moduleName = args[0];
			
			boolean foundModule = false;
			
			for(Module module : Vestige.getInstance().getModuleManager().getModules()) {
				if(module.getName().equalsIgnoreCase(moduleName)) {
					module.toggle();
					
					Vestige.getInstance().addChatMessage((module.isEnabled() ? "Enabled" : "Disabled") + " " + module.getName());
					
					foundModule = true;
					break;
				}
			}
			
			if(!foundModule) {
				Vestige.getInstance().addChatMessage("Error : Could not find module " + moduleName + ".");
			}
			
		}
	}

}
