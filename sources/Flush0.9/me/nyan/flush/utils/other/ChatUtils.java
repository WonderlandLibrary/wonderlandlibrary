package me.nyan.flush.utils.other;

import me.nyan.flush.Flush;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class ChatUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void println(Object o) {
        println(String.valueOf(o));
    }

    public static void println(String message) {
        if (mc.thePlayer == null) {
            return;
        }
        mc.thePlayer.addChatMessage(new ChatComponentText(message.isEmpty() ? "" : "§7[§9" + Flush.NAME + "§7] §f" + message));
    }

    public static void println() {
        println("");
    }
}
