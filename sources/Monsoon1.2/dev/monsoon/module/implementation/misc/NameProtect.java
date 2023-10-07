package dev.monsoon.module.implementation.misc;

import dev.monsoon.module.enums.Category;
import dev.monsoon.Monsoon;
import dev.monsoon.event.Event;
import dev.monsoon.event.listeners.EventPacket;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Keyboard;

import dev.monsoon.module.base.Module;
import dev.monsoon.util.misc.Timer;

public class NameProtect extends Module {

	
	Timer timer = new Timer();
	
	public NameProtect() {
		super("NameProtect", Keyboard.KEY_NONE, Category.MISC);
	}
	
	public void onEnable() {

	}
	
	public void onDisable() {

	}

	@Override
	public void onEvent(Event e) {
		if(e instanceof EventPacket && e.isIncoming()) {
			if (((EventPacket) e).getPacket() instanceof S02PacketChat) {
				S02PacketChat chat = (S02PacketChat) ((EventPacket) e).getPacket();
				IChatComponent chatComponent = chat.func_148915_c();
				if (chatComponent instanceof ChatComponentText && chat.isChat() && mc.theWorld != null && mc.thePlayer != null && mc.thePlayer.getName() != null) {
					String text = chatComponent.getFormattedText().replace(mc.thePlayer.getName(), getNewName());
					//String text = chatComponent.getFormattedText().replace(Minecraft.getMinecraft().thePlayer.getName(), Minecraft.getMinecraft().thePlayer.getName() + " (" + EnumChatFormatting.AQUA + Monsoon.monsoonUsername + EnumChatFormatting.RESET + ")");
					((EventPacket<?>) e).setPacket(new S02PacketChat(new ChatComponentText(text)));
				}
			}
		}
	}

	public String getNewName() {
		return EnumChatFormatting.AQUA + Monsoon.monsoonUsername + EnumChatFormatting.RESET;
	}

}