package epsilon.botting.cmds;

import epsilon.botting.BotCommand;

public class ConsoleDelay extends BotCommand {

	public static int msDelay = 1;
	
	public ConsoleDelay() {
		super("JoinDelay", "Lets you set the delay in MS", "delay", "d");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		msDelay = Integer.parseInt(args[0]);
		
	}
	

}
