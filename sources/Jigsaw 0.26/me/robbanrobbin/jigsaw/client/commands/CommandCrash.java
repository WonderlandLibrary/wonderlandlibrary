package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class CommandCrash extends Command {

	@Override
	public void run(String[] commands) {
		
	}

	@Override
	public String getActivator() {
		return ".crash";
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
