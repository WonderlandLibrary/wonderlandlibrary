package club.async.util;

import club.async.interfaces.MinecraftInterface;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public final class ChatUtil implements MinecraftInterface {

    public static void addChatMessage(String message) {
        if(Minecraft.getMinecraft().thePlayer != null) {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(ChatFormatting.RED + "Async" + ChatFormatting.GRAY + " >>" + ChatFormatting.RESET + " " + message));
        }
    }

}
