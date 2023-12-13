package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.commands.option.CommandOption;
import net.minecraft.network.play.client.CPacketPlayer;

public class CommandDamage extends Command {

	@Override
	public void run(String[] commands) {
		double dmg;
		try {
			dmg = Double.parseDouble(commands[1]);
		} catch (Exception e) {
			dmg = 0.5;
		}

		if (dmg <= 0) {
			addResult("§cDamage value must be more than 0!");
			return;
		}

		double posX = mc.player.posX;
		double posY = mc.player.posY;
		double posZ = mc.player.posZ;

		for (int i = 0; (double) i < 80.0d + 40.0d * (dmg - 0.5D); ++i) {
			mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY + 0.049D, posZ, false));
			mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY, posZ, false));
		}
		mc.player.connection.sendPacket(new CPacketPlayer.Position(posX, posY, posZ, true));
		// mc.player.jump();
		return;
	}

	@Override
	public String getActivator() {
		return "damage";
	}

	@Override
	public String getDesc() {
		return "Makes you take damage in the form of falldamage";
	}

	@Override
	public CommandOption[] setOptions() {
		// TODO Auto-generated method stub
		return null;
	}
}
