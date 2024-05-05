package rip.helium;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import rip.helium.cheat.impl.visual.Console;

public class ChatUtil {

    public static void chat(String msg) {
        Console.output.clear();
        Minecraft.getMinecraft().thePlayer
                .addChatMessage(new ChatComponentText("§7> §f" + msg));
        Console.output.add(msg);
    }

    public static void chatNoPrefix(String msg) {
        Minecraft.getMinecraft().thePlayer
                .addChatMessage(new ChatComponentText("§7> §f" + msg));
    }

}
