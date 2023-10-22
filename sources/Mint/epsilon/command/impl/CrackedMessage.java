package epsilon.command.impl;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.command.Command;
import epsilon.modules.Module;


public class CrackedMessage extends Command {

	public static String msg = "";
	
	public CrackedMessage() {
		super("Cracked", "Lets you set the message for bots", "message", "cm");
	}

	@Override
	public void onCommand(String[] args, String msg) {
		if(args.length > 0) {
			
			this.name = msg;
			Epsilon.addChatMessage("CrackedRaper accounts will now say: " + msg);
			
		}
	}
	
	public static String getMessage() {
		return msg;
	}

	
}
