package com.enjoytheban.utils;

import com.enjoytheban.Client;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class Helper {

	// shorter way to get minecraft.getminecraft()
	public static Minecraft mc = Minecraft.getMinecraft();

	// Method to msg the player something
	public static void sendMessageOLD(String msg) {
		mc.thePlayer.addChatMessage(new ChatComponentText(String.format("%s%s",
				EnumChatFormatting.BLUE + Client.instance.name + EnumChatFormatting.GRAY + ": ", msg)));
	}
	
	public static void sendMessage(String message) {
		new ChatUtils.ChatMessageBuilder(true, true).appendText(message).setColor(EnumChatFormatting.GRAY).build()
				.displayClientSided();
	}
	
	public static void sendMessageWithoutPrefix(String message) {
		new ChatUtils.ChatMessageBuilder(false, true).appendText(message).setColor(EnumChatFormatting.GRAY).build()
				.displayClientSided();
	}

	// Grabs the server the player is on
	public static boolean onServer(String server) {
		return !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains(server);
	}
}
