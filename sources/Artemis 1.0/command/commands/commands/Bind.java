package artemis.command.commands;

import org.lwjgl.input.Keyboard;

import artemis.Artemis;
import artemis.module;
import artemis.command.Command;

public class Bind extends Command{

	@Override
	public String getAlias() {
		return "bind";
	}

	@Override
	public String getDescription() {
		return "Allows user to change keybind of hack.";
	}

	@Override
	public String getSyntax() {
		return ".bind set [MOD] [Key] | .bind del [MOD] | .bind clear";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		if(args[0].equalsIgnoreCase("set")){
			args[2] = args[2].toUpperCase();
			int key = Keyboard.getKeyIndex(args[2]);
			
			for(module m: Artemis.getModules()){
				if(args[1].equalsIgnoreCase(m.getName())){
					m.setKey(Keyboard.getKeyIndex(Keyboard.getKeyName(key)));
					Artemis.addChatMessage(args[1] + " has been binded to " + args[2]);
				}
			}
			
		}else if(args[0].equalsIgnoreCase("del")){
			for(module m: Artemis.getModules()){
				if(m.getName().equalsIgnoreCase(args[1])){
					m.setKey(0);
					Artemis.addChatMessage(args[1] + " has been unbinded");
				}
			}
		}else if(args[0].equalsIgnoreCase("clear")){
			for(module m: Artemis.getModules()){
				m.setKey(0);
			}
			Artemis.addChatMessage("All keys cleared");
		}
	}

}
