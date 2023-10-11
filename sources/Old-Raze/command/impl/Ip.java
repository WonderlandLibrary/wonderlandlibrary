package markgg.command.impl;

import markgg.RazeClient;
import markgg.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;

public class Ip extends Command {

	public Ip() {
		super("ip", "Shows the servers ip", "ip", "i");
	}

	@Override
	public void onCommand(String[] args, String command) {
		if(!Minecraft.getMinecraft().isSingleplayer())
			RazeClient.addChatMessage("You are playing on : " + EnumChatFormatting.GREEN + Minecraft.getMinecraft().getCurrentServerData().serverIP);
		else
			RazeClient.addChatMessage("You are playing on a singleplayer world.");
	}

}
