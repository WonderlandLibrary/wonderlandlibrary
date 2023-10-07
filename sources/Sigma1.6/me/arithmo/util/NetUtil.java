/*
 * Decompiled with CFR 0_122.
 */
package me.arithmo.util;

import me.arithmo.util.MinecraftUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;

public class NetUtil
implements MinecraftUtil {
    public static void sendPacketNoEvents(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(packet);
    }

    public static void sendPacket(Packet packet) {
        NetUtil.mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}

