package Velo.impl.Command;

import Velo.api.Command.Command;
import Velo.api.Module.Module;
import Velo.api.Module.ModuleManager;
import Velo.api.Util.Other.ChatUtil;

public class Toggle extends Command {
	
	public Toggle() {
		super("Toggle", "Toggle a module by command xd", "toggle <module>", "t");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(args.length > 0) {
			String moduleName = args[0];
			
			boolean foundModule = false;
			
			for(Module module : ModuleManager.modules) {
				if(module.name.equalsIgnoreCase(moduleName)) {
					module.toggle();
					
					ChatUtil.addChatMessage((module.isEnabled() ? "Enabled" : "Disabled") + " " + module.name);
					
					foundModule = true;
					break;
				}
			}
			
			if(!foundModule) {
				ChatUtil.addChatMessage("Could not find module.");
			}
		}
	}
}
