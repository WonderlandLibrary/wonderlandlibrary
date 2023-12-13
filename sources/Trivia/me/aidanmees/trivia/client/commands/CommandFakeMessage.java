package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;

public class CommandFakeMessage extends Command {

	@Override
	public void run(String[] commands) {
		String toSay = "";
		for (int i = 0; i < commands.length; i++) {
			if (i == 0) {
				continue;
			}
			toSay += commands[i];
			toSay += " ";
		}
		toSay = toSay.replaceAll("&", "§");
		trivia.chatMessage("", toSay);
	}

	@Override
	public String getActivator() {
		return ".fakemsg";
	}

	@Override
	public String getSyntax() {
		return ".fakemsg <message>";
	}

	@Override
	public String getDesc() {
		return "Enables you to fake chatmessages to false-report people for spam and swearing for example. Tip: You can use '&' for formatting.";
	}
}
