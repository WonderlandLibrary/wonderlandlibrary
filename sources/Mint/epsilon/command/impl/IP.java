package epsilon.command.impl;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.command.Command;
import epsilon.modules.Module;


public class IP extends Command {

	public IP() {
		super("IP", "Tells you the IP of the server ur on", "ip");
	}

	@Override
	public void onCommand(String[] args, String command) {
		Epsilon.addChatMessage(Epsilon.ip + " is the servers join ip!");
		if(Epsilon.canonIP==null) return;
		Epsilon.addChatMessage(Epsilon.canonIP + " is the servers ip!");
	
	}
	
}
