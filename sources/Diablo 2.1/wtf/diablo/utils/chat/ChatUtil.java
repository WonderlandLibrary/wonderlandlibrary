package wtf.diablo.utils.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import wtf.diablo.Diablo;
import wtf.diablo.utils.render.ColorUtil;

public class ChatUtil {

    public static void log(String message) {
        addChat("§8[§d" + Diablo.name + "§8" +  "] §6" + message);
    }

    public static void addChat(String text) {
        if (Minecraft.getMinecraft().theWorld != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(text));
        }
    }
}
