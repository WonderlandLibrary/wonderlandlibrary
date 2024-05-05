package me.aidanmees.trivia.client.chat;

import me.aidanmees.trivia.client.main.trivia;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class ChatMananger {

	private Minecraft mc = Minecraft.getMinecraft();

	public ChatMananger() {

	}

	public void onMessageSent(C01PacketChatMessage packet) throws Exception {
		if (packet.getMessage().startsWith(".")) {
			String[] commands = packet.getMessage().trim().split("\\s++");
			boolean success = trivia.getCommandManager().onCommand(commands[0], commands);
			if (!success) {
				trivia.chatMessage("§cCould not find command!");
			}
			// if(command.equalsIgnoreCase(".save")) {
			// AutoBuild.saveDone(commands[1]);
			// return;
			// }
			// if(command.equalsIgnoreCase(".load")) {
			// AutoBuild.loadConf(commands[1]);
			// return;
			// }
		} else {
			mc.thePlayer.sendQueue.getNetworkManager().sendPacketFinal(packet);
		}
	}

}
