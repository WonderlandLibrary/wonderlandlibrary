/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.util.misc;

import me.arithmo.util.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class ChatUtil
implements MinecraftUtil {
    public static void printChat(String text) {
        ChatUtil.mc.thePlayer.addChatComponentMessage(new ChatComponentText(text));
    }

    public static void sendChat_NoFilter(String text) {
        ChatUtil.mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(text));
    }

    public static void sendChat(String text) {
        ChatUtil.mc.thePlayer.sendChatMessage(text);
    }
}

