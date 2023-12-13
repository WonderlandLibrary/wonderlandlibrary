package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;

public class CommandServerVersion extends Command {

	@Override
	public void run(String[] commands) {
		if(mc.getCurrentServerData() == null) {
			trivia.chatMessage("§cYou are not on a server!");
			return;
		}
		trivia.chatMessage(mc.getCurrentServerData().serverName
				+ " : " + mc.getCurrentServerData().gameVersion);
	}

	@Override
	public String getActivator() {
		return ".version";
	}

	@Override
	public String getSyntax() {
		return ".version";
	}

	@Override
	public String getDesc() {
		return "Prints the server version";
	}
}
