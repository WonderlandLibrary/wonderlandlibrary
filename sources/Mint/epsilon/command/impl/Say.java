package epsilon.command.impl;

import org.lwjgl.input.Keyboard;

import epsilon.Epsilon;
import epsilon.command.Command;
import epsilon.modules.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;


public class Say extends Command {

	public Say() {
		super("Say", "Say things in chat", "say", "s");
	}

	@Override
	public void onCommand(String[] args, String command) {
		Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(String.join(" ", args)));
	}
	
}
