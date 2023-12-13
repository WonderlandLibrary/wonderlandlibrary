package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

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
			trivia.chatMessage("§cDamage value must be more than 0!");
			return;
		}

		double posX = mc.thePlayer.posX;
		double posY = mc.thePlayer.posY;
		double posZ = mc.thePlayer.posZ;

		for (int i = 0; (double) i < 80.0d + 40.0d * (dmg - 0.5D); ++i) {
			mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(posX, posY + 0.049D, posZ, false));
			mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(posX, posY, posZ, false));
		}
		mc.thePlayer.sendQueue.addToSendQueue(new C04PacketPlayerPosition(posX, posY, posZ, true));
		// mc.thePlayer.jump();
		return;
	}

	@Override
	public String getActivator() {
		return ".damage";
	}

	@Override
	public String getSyntax() {
		return ".damage, .damage <hearts>";
	}

	@Override
	public String getDesc() {
		return "Makes you take damage in the form of falldamage";
	}
}
