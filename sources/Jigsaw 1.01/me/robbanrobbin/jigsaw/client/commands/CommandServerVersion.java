package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;

public class CommandServerVersion extends Command {

	@Override
	public void run(String[] commands) {
		if(mc.getCurrentServerData() == null) {
			addResult("§cYou are not on a server!");
			return;
		}
		addResult(mc.getCurrentServerData().serverName
				+ " : " + mc.getCurrentServerData().gameVersion);
	}

	@Override
	public String getActivator() {
		return "version";
	}

	@Override
	public String getSyntax() {
		return "version";
	}

	@Override
	public String getDesc() {
		return "Prints the server version";
	}

	@Override
	public CommandOption[] setOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
