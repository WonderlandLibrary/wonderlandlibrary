package epsilon.botting.cmds;

import java.util.regex.Pattern;

import epsilon.botting.BotCommand;

public class ConsolePort extends BotCommand {

	private static String port = 25565+"";
	
	public ConsolePort() {
		super("Port", "Sets the port", "port");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		
		setPort(args[0]);
		print("Bots will now join " + args[0]);
		
	}

	public static String getPort() {
		return port;
	}
	
	
	private void setPort(String args) {
		this.port = args;
	}
	
	
	

}
