package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.modules.Player.FakeHacker;
import net.minecraft.entity.player.EntityPlayer;

public class CommandFakehacker extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length == 1) {
			trivia.chatMessage("Enter a name!");
			return;
		}
		String name = commands[1];
		EntityPlayer player = mc.theWorld.getPlayerEntityByName(name);
		if (player == null) {
			trivia.chatMessage("That player could not be found!");
			return;
		}
		if (FakeHacker.isFakeHacker(player)) {
			FakeHacker.removeHacker(player);
		} else {
			FakeHacker.fakeHackers.add(name);
		}
		trivia.chatMessage("Added player §6\"" + name + "§7 as a fakehacker");
	}

	@Override
	public String getActivator() {
		return ".fakehacker";
	}

	@Override
	public String getSyntax() {
		return ".fakehacker <name>";
	}

	@Override
	public String getDesc() {
		return "Adds a player as a fakehacker for the mod \"fakehacker\"";
	}
}
