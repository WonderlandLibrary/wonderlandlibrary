package epsilon.command.impl;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.command.Command;
import epsilon.modules.Module;


public class Cracked extends Command {

	public static String name = "Spiny";
	
	public Cracked() {
		super("Cracked", "Lets you set the name infront of random numbers in cracked gen spam join", "crack", "c");
	}

	@Override
	public void onCommand(String[] args, String name) {
		if(args.length > 0) {
			
			
			this.name = args[0];
			Epsilon.addChatMessage("CrackedRaper accounts will now join with that name, ex: " + args[0] + "634vs");
			
		}
	}

	
}
