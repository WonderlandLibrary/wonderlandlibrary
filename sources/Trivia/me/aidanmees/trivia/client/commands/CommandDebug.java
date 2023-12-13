package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;

public class CommandDebug extends Command {

	@Override
	public void run(String[] commands) {
		trivia.debugMode = !trivia.debugMode;
	}

	@Override
	public String getActivator() {
		return ".debug";
	}

	@Override
	public String getSyntax() {
		return ".debug";
	}

	@Override
	public String getDesc() {
		return "Turns debug mode on";
	}
}
