package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;

public class CommandCommands extends Command {

	@Override
	public void run(String[] commands) {
		trivia.chatMessage("§6These are all available commands:");
		for (Command cmd : trivia.getCommandManager().commands) {
			trivia.chatMessage("§b§l" + cmd.getActivator());
		}
	}

	@Override
	public String getActivator() {
		return ".commands";
	}

	@Override
	public String getSyntax() {
		return ".commands";
	}

	@Override
	public String getDesc() {
		return "Lists all commands";
	}
}
