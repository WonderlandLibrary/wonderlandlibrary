// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils;

import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.augustus.Augustus;
import net.augustus.utils.interfaces.MC;

public class ChatUtil implements MC
{
    public static void sendChat(final String s) {
        ChatUtil.mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText("§6[§9" + Augustus.getInstance().getName() + "§6] §7" + s));
    }
}
