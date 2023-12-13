package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class CommandHypickle extends Command {

	@Override
	public void run(String[] commands) {
		
		if(commands.length > 1) {
			if(commands[1].equals("133742069flyxd")) {
				Jigsaw.getModuleByName("Flight").setModeSilent("Hypixel");
			}
		}
		
	}

	@Override
	public String getActivator() {
		return ".test";
	}

	@Override
	public String getSyntax() {
		return ".test";
	}

	@Override
	public String getDesc() {
		return "Test";
	}

	@Override
	public CommandOption[] setOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
