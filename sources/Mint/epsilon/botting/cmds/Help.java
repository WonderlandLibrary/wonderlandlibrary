package epsilon.botting.cmds;

import epsilon.botting.BotCommand;

public class Help extends BotCommand {

	public Help() {
		super("Help", "Provides help", "helo", "h");
	}

	@Override
	public void onCommand(String[] args, String command) {
		
		help();
		
	}

	public void help() {
		print("");
		print("-----------------------------------------------");
		print("MintStresser has many commands!");
		print("Use 'ip <ip>' to set the ip!");
		print("'.port <port>' to set the port");
		print("If you are unsure of a servers port, do 'portget <ip>' ! Or just use 25565");
		print("'.prefix <name>' to set the bots prefix");
		print("'.start' to begin the test!");
		print("'.end' to cut connections and end");
		print("'.message <message>' to set the spam msg");
		print("'.spamd <spamDelayInMS>' delay between spam");
		print("'.live <true/false>' to keep connections");
		print("'.register <msg>' to set the msg sent on join");
		print("'.joindelay <delayInMS>' this is obvious ");
		print("");
		print("Currently we only have two crasher types, disconnect and bad packet kick");
		print("To toggle either do '.thread1 true' (thread1=disconnect), the other should be obvious");
		print("");
		print("From here it should be easy to use!");
		print("There are many more commands I did not cover!");
		print("For a list of cmds, do '.cmd page<num>'");
		print("Do 'info <command> ' to get its description and helpful tips");
		print("-----------------------------------------------");
		print("");
		
	}

}
