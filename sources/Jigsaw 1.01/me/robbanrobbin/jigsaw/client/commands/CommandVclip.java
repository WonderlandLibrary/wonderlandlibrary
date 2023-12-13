package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;

public class CommandVclip extends Command {

	@Override
	public void run(String[] commands) {
		double dist = 0;
		try {
			dist = Double.parseDouble(commands[1]);
		} catch (NumberFormatException e) {
			addResult("§cPlease enter a valid number!");
			return;
		}
		mc.player.setPosition(mc.player.posX, mc.player.posY + dist, mc.player.posZ);
		addResult("Teleported you " + dist + " blocks " + (dist < 0 ? "down!" : "up!"));
	}

	@Override
	public String getActivator() {
		return "vclip";
	}

	@Override
	public String getDesc() {
		return "Teleports you vertically. Enables you to go through blocks vertically too!";
	}

	@Override
	public CommandOption[] setOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
