/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.server.S32PacketConfirmTransaction
 */
package net.ccbluex.liquidbounce.utils;

import java.util.ArrayList;
import net.ccbluex.liquidbounce.event.EventTarget;
import net.ccbluex.liquidbounce.event.Listenable;
import net.ccbluex.liquidbounce.event.PacketEvent;
import net.ccbluex.liquidbounce.event.TickEvent;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.timer.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class PacketUtils2
extends MinecraftInstance
implements Listenable {
    private static final ArrayList<Packet<INetHandlerPlayServer>> packets = new ArrayList();
    private static final MSTimer packetTimer = new MSTimer();
    private static final MSTimer wdTimer = new MSTimer();
    public static int inBound;
    public static int outBound;
    public static int avgInBound;
    public static int avgOutBound;
    private static int transCount;
    private static int wdVL;

    private static boolean isInventoryAction(short action) {
        return action > 0 && action < 100;
    }

    public static boolean isWatchdogActive() {
        return wdVL >= 8;
    }

    private static void handlePacket(Packet<?> packet) {
        if (packet.getClass().getSimpleName().startsWith("C")) {
            ++outBound;
        } else if (packet.getClass().getSimpleName().startsWith("S")) {
            ++inBound;
        }
        if (packet instanceof S32PacketConfirmTransaction && !PacketUtils2.isInventoryAction(((S32PacketConfirmTransaction)packet).func_148890_d())) {
            ++transCount;
        }
    }

    public static void sendPacketNoEvent(Packet<INetHandlerPlayServer> packet) {
        packets.add(packet);
        mc.func_147114_u().func_147297_a(packet);
    }

    public static boolean handleSendPacket(Packet<?> packet) {
        if (packets.contains(packet)) {
            packets.remove(packet);
            PacketUtils2.handlePacket(packet);
            return true;
        }
        return false;
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        PacketUtils2.handlePacket(event.getPacket());
    }

    @EventTarget
    public void onTick(TickEvent event) {
        if (packetTimer.hasTimePassed(1000L)) {
            avgInBound = inBound;
            avgOutBound = outBound;
            outBound = 0;
            inBound = 0;
            packetTimer.reset();
        }
        if (PacketUtils2.mc.field_71439_g == null || PacketUtils2.mc.field_71441_e == null) {
            wdVL = 0;
            transCount = 0;
            wdTimer.reset();
        } else if (wdTimer.hasTimePassed(100L)) {
            int n = transCount > 0 ? 1 : -1;
            transCount = 0;
            if ((wdVL += n) > 10) {
                wdVL = 10;
            }
            if (wdVL < 0) {
                wdVL = 0;
            }
            wdTimer.reset();
        }
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    static {
        outBound = 0;
        avgOutBound = 0;
        transCount = 0;
        wdVL = 0;
    }
}

