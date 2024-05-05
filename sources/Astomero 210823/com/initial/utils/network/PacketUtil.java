package com.initial.utils.network;

import net.minecraft.client.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;

public class PacketUtil
{
    private static final Minecraft mc;
    
    public static void sendPacket(final Packet packet) {
        PacketUtil.mc.getNetHandler().addToSendQueue(packet);
    }
    
    public static void sendPacketSilent(final Packet packet) {
        PacketUtil.mc.thePlayer.sendQueue.addToSilentQueue(packet);
    }
    
    public static void sendC04(final double x, final double y, final double z, final boolean ground, final boolean silent) {
        if (silent) {
            sendPacketSilent(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, ground));
        }
        else {
            sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, ground));
        }
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
