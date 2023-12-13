package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;

public class CommandVclip extends Command {

	@Override
	public void run(String[] commands) {
		double dist = 0;
		try {
			dist = Double.parseDouble(commands[1]);
		} catch (NumberFormatException e) {
			trivia.chatMessage("§cPlease enter a valid number!");
			return;
		}
		mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + dist, mc.thePlayer.posZ);
		trivia.chatMessage("Teleported you " + dist + " blocks " + (dist < 0 ? "down!" : "up!"));
	}

	@Override
	public String getActivator() {
		return ".vclip";
	}

	@Override
	public String getSyntax() {
		return ".vclip <distance>";
	}

	@Override
	public String getDesc() {
		return "Teleports you vertically. Enables you to go through blocks vertically too!";
	}
}
