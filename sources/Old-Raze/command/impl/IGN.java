package markgg.command.impl;

import markgg.RazeClient;
import markgg.command.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;

public class IGN extends Command {

	public IGN() {
		super("ign", "Shows your ingame name", "ign", "ig");
	}

	@Override
	public void onCommand(String[] args, String command) {
		RazeClient.addChatMessage("Your username is: " + EnumChatFormatting.GREEN + Minecraft.getMinecraft().session.getUsername());
	}

}
