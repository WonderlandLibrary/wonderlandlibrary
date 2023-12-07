package Velo.api.Util.Other;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {
	
	public static void addChatMessage(String message) {
		message = "\2478" + "Invasion" + "\2477: " + message;
		
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
	}
	
	public static void addChatMessage3(String message) {
		message = "\2478" + "Dev Info" + "\2477: " + message;
		
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
	}
	
	public static void addChatMessage2(String message) {
		Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
	}
	
}
