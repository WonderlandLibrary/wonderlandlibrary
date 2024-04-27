/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.Unpooled
 */
package net.ccbluex.liquidbounce.features.special;

import io.netty.buffer.Unpooled;
import net.ccbluex.liquidbounce.api.minecraft.network.IPacket;
import net.ccbluex.liquidbounce.api.minecraft.network.play.client.ICPacketCustomPayload;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;

public class AntiForge
extends MinecraftInstance
implements Listenable {
    public static boolean blockFML;
    public static boolean blockProxyPacket;
    public static boolean enabled;
    public static boolean blockPayloadPackets;

    @EventTarget
    public void onPacket(PacketEvent packetEvent) {
        IPacket iPacket = packetEvent.getPacket();
        if (enabled && !mc.isIntegratedServerRunning()) {
            try {
                if (blockProxyPacket && iPacket.getClass().getName().equals("net.minecraftforge.fml.common.network.internal.FMLProxyPacket")) {
                    packetEvent.cancelEvent();
                }
                if (blockPayloadPackets && classProvider.isCPacketCustomPayload(iPacket)) {
                    ICPacketCustomPayload iCPacketCustomPayload = iPacket.asCPacketCustomPayload();
                    if (!iCPacketCustomPayload.getChannelName().startsWith("MC|")) {
                        packetEvent.cancelEvent();
                    } else if (iCPacketCustomPayload.getChannelName().equalsIgnoreCase("MC|Brand")) {
                        iCPacketCustomPayload.setData(classProvider.createPacketBuffer(Unpooled.buffer()).writeString("vanilla"));
                    }
                }
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    static {
        enabled = true;
        blockFML = true;
        blockProxyPacket = true;
        blockPayloadPackets = true;
    }
}

