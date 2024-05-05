package me.aidanmees.trivia.client.commands;

import java.util.List;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;

public class CommandGround extends Command {

	@Override
	public void run(String[] args) {
		if(args.length <= 1) {
			for(int i = 0; i < 3; i++) {
			 mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition( mc.thePlayer.posX, mc.thePlayer.posY - 0.04, mc.thePlayer.posZ, mc.thePlayer.onGround));
		     mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition( mc.thePlayer.posX, mc.thePlayer.posY + 0.28, mc.thePlayer.posZ, mc.thePlayer.onGround));
		}	
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ, mc.thePlayer.onGround));
			trivia.chatMessage("You should now be in the ground!");
			} else if(args.length > 1 && args[1].equalsIgnoreCase("unsolid")) {
				for(int i = 0; i < 3; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.061, mc.thePlayer.posZ, mc.thePlayer.onGround));
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.3, mc.thePlayer.posZ, mc.thePlayer.onGround));
			}
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ, mc.thePlayer.onGround));
				trivia.chatMessage("You should now be in the unsolid object!");
			} else {
				trivia.chatMessage(".ground <unsolid>");
			}
		
}

	@Override
	public String getActivator() {
		return ".ground";
	}

	@Override
	public String getSyntax() {
		return ".ground, .ground <unsolid>";
	}

	@Override
	public String getDesc() {
		return "Gets u to the ground";
	}
}
