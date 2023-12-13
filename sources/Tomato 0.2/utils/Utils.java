/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.Cheese;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Utils {
    public static void addChatMessage(String message) {
        String toSend = "\u00a7e[Cheese] " + message;
        Cheese.getInstance();
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\u00a77[\u00a7c" + Cheese.getClient_Name() + "\u00a77] " + message));
    }
}

