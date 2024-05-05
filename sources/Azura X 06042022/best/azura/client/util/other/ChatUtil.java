package best.azura.client.util.other;

import best.azura.client.impl.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtil {
	private static final Minecraft mc = Minecraft.getMinecraft();

	public static void sendChat(String message) {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(Client.PREFIX + message));
	}

	public static void sendDebug(String message) {
		mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§c§o" + message));
	}

	public static void sendError(String error) {
		sendChat("§c" + error);
	}
}