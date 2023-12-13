package me.robbanrobbin.jigsaw.client.chat;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.CPacketChatMessage;

public class ChatMananger {

	private Minecraft mc = Minecraft.getMinecraft();

	public ChatMananger() {

	}

	public void onMessageSent(CPacketChatMessage packet) throws Exception {
		if (packet.getMessage().startsWith(".") && !Jigsaw.ghostMode) {
			Jigsaw.getCommandManager().tryRunCommand(packet.getMessage().substring(1));
			
			// if(command.equalsIgnoreCase(".save")) {
			// AutoBuild.saveDone(commands[1]);
			// return;
			// }
			// if(command.equalsIgnoreCase(".load")) {
			// AutoBuild.loadConf(commands[1]);
			// return;
			// }
			
		} else {
			mc.player.connection.getNetworkManager().sendPacketFinal(packet);
		}
	}

}
