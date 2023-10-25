/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class PacketUtil {
    public static void sendPacket(Packet packet) {
        Minecraft.getMinecraft();
        Minecraft.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public static void sendPackets(Packet packet, int n) {
        int n2 = 0;
        while (n2 < n) {
            Minecraft.getMinecraft();
            Minecraft.thePlayer.sendQueue.addToSendQueue(packet);
            ++n2;
        }
    }
}

