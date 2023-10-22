package epsilon.botting.cmds;

import epsilon.botting.BotCommand;

public class ConsoleIP extends BotCommand {

	private static String ip = "127.0.0.1";
	
	public ConsoleIP() {
		super("Ip", "Lets you set the join IP", "ip");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		setIP(args[0]);
		print("Bots will now join " + args[0]);
		
	}

	public static String getIP() {
		return ip;
	}
	
	private void setIP(String ip) {
		this.ip = ip;
	}
	
	
	

}
