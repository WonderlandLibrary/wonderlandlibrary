package wtf.diablo.utils.chat;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class LogUtils {

    public static void addChatMessage(String message) {
        message = "§4" + "Diablo" + "§4: §8" + message;

        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void addChatMessageDevInfo(String message) {
        message = "\2478" + "Dev Info" + "\2477: " + message;

        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }

    public static void addChatMessageNormal(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(message));
    }
}
