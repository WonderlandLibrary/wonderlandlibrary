package markgg.command.impl;

import markgg.RazeClient;
import markgg.command.Command;
import markgg.modules.Module;
import net.minecraft.util.EnumChatFormatting;

public class Toggle extends Command {

	public Toggle() {
		super("Toggle", "Toggles a specific module.", "toggle <name>", "t");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if (args.length > 0) {
			String moduleName = args[0];

			boolean foundModule = false;

			for (Module module : RazeClient.getModuleManager().getModules().values()) {
				if (module.name.equalsIgnoreCase(moduleName)) {
					module.toggle();
					
					RazeClient.addChatMessage((module.isEnabled() ? "Enabled" : "Disabled") + " " + module.name);
					
					foundModule = true;
					break;
				} 
			} 
			if (!foundModule)
				RazeClient.addChatMessage("Error finding module: " + EnumChatFormatting.RED + "could not find module.");
		} else
			RazeClient.addChatMessage("Error: " + EnumChatFormatting.RED + "module parameter cannot be empty!");
	}

}
