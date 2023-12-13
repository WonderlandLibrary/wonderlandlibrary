package me.aidanmees.trivia.client.commands;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;

public class CommandBugUp extends Command {

	
	
	@Override
	public void run(String[] commands) {
		
	bugUp();
	
	
	}


    public static void bugUp() {
    	Minecraft mc = Minecraft.getMinecraft();
        for (int i = 0; i < 4; i++) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                    mc.thePlayer.posY + 0.1, mc.thePlayer.posZ, false));
        }


   
}

	@Override
	public String getActivator() {
		return ".bugup";
	}

	@Override
	public String getSyntax() {
		return "bugup";
	}

	@Override
	public String getDesc() {
		return "Allows u to go bug up";
	}
}
