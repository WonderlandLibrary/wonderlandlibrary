package me.aidanmees.trivia.client.commands;

import java.io.IOException;

import me.aidanmees.trivia.client.main.trivia;
import me.aidanmees.trivia.client.tools.HastebinAPI;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class CommandHastebin extends Command {

	@Override
	public void run(String[] commands) {
		String toSay = "";
		for (int i = 0; i < commands.length; i++) {
			if (i == 0) {
				continue;
			}
			toSay += commands[i];
			toSay += " ";
		} 
		try {
			IChatComponent e = IChatComponent.Serializer.jsonToComponent("[\"\",{\"text\":\"§8[§2§lRC§8] §fClick here for the hastebin link!\",\"color\":\"green\",\"bold\":true,\"underlined\":false,\"clickEvent\":{\"action\":\"open_url\",\"value\":\""+ HastebinAPI.uploadToHastebin(toSay) +"\"}}]");
			
			mc.thePlayer.addChatComponentMessage(e);
		} catch (IOException e) {
			trivia.chatMessage("Could not cleate the url! Please try .hastebin <Message>");
			e.printStackTrace();
		}
	}

	@Override
	public String getActivator() {
		return ".hastebin";
	}

	@Override
	public String getSyntax() {
		return ".hastebin <message>";
	}

	@Override
	public String getDesc() {
		return "Hastebin's a message for you";
	}
}
