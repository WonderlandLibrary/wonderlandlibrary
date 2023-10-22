package epsilon.command.impl;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.command.Command;
import epsilon.modules.Module;


public class Port extends Command {

	public Port() {
		super("Port", "Tells you the Port of the server ur on", "port");
	}

	@Override
	public void onCommand(String[] args, String command) {
		Epsilon.addChatMessage(Epsilon.port + " is the servers port");
	
	}
	
}
