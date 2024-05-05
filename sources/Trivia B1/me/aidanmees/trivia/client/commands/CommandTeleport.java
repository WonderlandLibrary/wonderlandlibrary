package me.aidanmees.trivia.client.commands;

import java.util.List;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CommandTeleport extends Command {

	@Override
	public void run(String[] args) {
		if (args.length > 3) {
			float	posX,
					posY,
					posZ;

			try {
				posX = args[1].contains("~") ? (float) mc.thePlayer.posX + (args[1].length() == 1 ? 0 : Float.parseFloat(args[1].substring(1))) : Float.parseFloat(args[1]);
				posY = args[2].contains("~") ? (float) mc.thePlayer.posY + (args[2].length() == 1 ? 0 : Float.parseFloat(args[2].substring(1))) : Float.parseFloat(args[2]);
				posZ = args[3].contains("~") ? (float) mc.thePlayer.posZ + (args[3].length() == 1 ? 0 : Float.parseFloat(args[3].substring(1))) : Float.parseFloat(args[3]);	
				
				mc.thePlayer.setLocationAndAngles(posX, posY, posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, true));
				
				trivia.chatMessage("Teleported to: " + (int) mc.thePlayer.posX + " " + (int) mc.thePlayer.posY + " " + (int) mc.thePlayer.posZ + "."); 
			} catch (NumberFormatException e) {
				trivia.chatMessage("Couldn't parse an integer from: " + e.getCause() + ".");
			}
		} else if (args.length > 1) {
			boolean success = false;
			for (EntityPlayer e : (List<EntityPlayer>) mc.theWorld.playerEntities) {
				if (e.getName().equalsIgnoreCase(args[1])) {
					success = true;
					int posX = (int) e.posX,
						posY = (int) e.posY,
						posZ = (int) e.posZ;
					
					mc.thePlayer.setLocationAndAngles(posX, posY, posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, true));
					
					trivia.chatMessage("Teleported to: " + (int) mc.thePlayer.posX + " " + (int) mc.thePlayer.posY + " " + (int) mc.thePlayer.posZ + ".");
					break;
				}
			}
			if (!success) {
				trivia.chatMessage("Could not find player by the name of: " + args[1] + ".");
			}
		} else {
			trivia.chatMessage("Please provide the X, Y, and Z coordinates, or a player's name.");
		}
	

}

	@Override
	public String getActivator() {
		return ".teleport";
	}

	@Override
	public String getSyntax() {
		return ".teleport, .teleport X Y Z";
	}

	@Override
	public String getDesc() {
		return "Allows u to TP";
	}
}
