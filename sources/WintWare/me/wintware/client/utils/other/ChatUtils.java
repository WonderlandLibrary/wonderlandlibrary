/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.mojang.realmsclient.gui.ChatFormatting
 */
package me.wintware.client.utils.other;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public class ChatUtils {
    public static void addChatMessage(String message) {
        Minecraft.getMinecraft();
        Minecraft.player.addChatMessage(new TextComponentString(ChatFormatting.GREEN + "[WW] " + ChatFormatting.RESET + message));
    }
}

