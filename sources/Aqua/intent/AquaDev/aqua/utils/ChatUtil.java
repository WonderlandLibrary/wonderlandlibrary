package intent.AquaDev.aqua.utils;

import intent.AquaDev.aqua.Aqua;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ChatUtil {
    public static void sendChatMessage(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(message));
    }

    public static void sendChatMessageWithPrefix(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText("\u00a77[\u00a73" + Aqua.name + "\u00a77]\u00a7f " + message));
    }

    public static void messageWithoutPrefix(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(message));
    }

    public static void sendChatInfo(String string) {
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText("\u00a77[\u00a7f" + Aqua.name + "\u00a77]\u00a7a " + string));
    }

    public static void sendChatError(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText("\u00a77[\u00a7f" + Aqua.name + "\u00a77]\u00a7c " + message));
    }

    public static void sendBlurFixMessage() {
        Minecraft.getMinecraft().thePlayer.addChatMessage((IChatComponent)new ChatComponentText(""));
    }
}
