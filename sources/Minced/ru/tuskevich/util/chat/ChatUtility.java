// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.util.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.Utility;

public class ChatUtility implements Utility
{
    public static String chatPrefix;
    
    public static void addChatMessage(final String message) {
        final Minecraft mc = ChatUtility.mc;
        Minecraft.player.sendMessage(new TextComponentString(ChatUtility.chatPrefix + message));
    }
    
    static {
        ChatUtility.chatPrefix = ChatFormatting.DARK_GRAY + "[" + ChatFormatting.WHITE + "MIN" + ChatFormatting.BLUE + "CED" + ChatFormatting.DARK_GRAY + "] " + ChatFormatting.RESET;
    }
}
