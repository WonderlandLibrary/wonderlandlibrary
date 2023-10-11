package markgg.command.impl;

import markgg.RazeClient;
import markgg.command.Command;
import markgg.modules.Module;
import markgg.modules.Module.Category;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.EnumChatFormatting;

public class Discord extends Command {

	public Discord() {
		super("discord", "Shows our discord invite link", "discord", "disc");
	}

	@Override
	public void onCommand(String[] args, String command) {
		RazeClient.addChatMessage("Our discord server: " + EnumChatFormatting.GREEN + "discord.gg/qynVdyQaXe");
	}

}
