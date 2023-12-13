package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class CommandDebug extends Command {

	@Override
	public void run(String[] commands) {
		Jigsaw.debugMode = !Jigsaw.debugMode;
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

	@Override
	public CommandOption[] setOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
