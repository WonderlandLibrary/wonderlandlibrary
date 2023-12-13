package vestige.command.impl;

import org.lwjgl.input.Keyboard;
import vestige.Vestige;
import vestige.api.module.Module;
import vestige.command.Command;

import java.util.Locale;

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

			for(Module module : Vestige.getInstance().getModuleManager().getModules()) {
				if(module.getName().equalsIgnoreCase(moduleName)) {
					module.getKeybind().setKey(Keyboard.getKeyIndex(keyName.toUpperCase()));

					Vestige.getInstance().addChatMessage(String.format("Bound %s to %s", module.getName(), keyName));
					foundModule = true;
					break;
				}
			}

			if(!foundModule) {
				Vestige.getInstance().addChatMessage("Could not find module.");
			}
			
		}

		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("clear")) {
				for(Module module : Vestige.getInstance().getModuleManager().getModules()) {
					module.getKeybind().setKey(Keyboard.KEY_NONE);
				}
				Vestige.getInstance().addChatMessage("Cleared all binds.");
			}
		}
	}

}
