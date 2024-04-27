/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.INetHandlerPlayServer
 *  net.minecraft.network.play.server.S32PacketConfirmTransaction
 */
package net.dev.important.utils;

import java.util.ArrayList;
import net.dev.important.event.EventTarget;
import net.dev.important.event.Listenable;
import net.dev.important.event.PacketEvent;
import net.dev.important.event.TickEvent;
import net.dev.important.utils.MinecraftInstance;
import net.dev.important.utils.timer.MSTimer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;

public class PacketUtils
extends MinecraftInstance
implements Listenable {
    public static int inBound;
    public static int outBound;
    public static int avgInBound;
    public static int avgOutBound;
    private static ArrayList<Packet<INetHandlerPlayServer>> packets;
    private static MSTimer packetTimer;
    private static MSTimer wdTimer;
    private static int transCount;
    private static int wdVL;

    private static boolean isInventoryAction(short action) {
        return action > 0 && action < 100;
    }

    public static boolean isWatchdogActive() {
        return wdVL >= 8;
    }

    @EventTarget
    public void onPacket(PacketEvent event) {
        PacketUtils.handlePacket(event.getPacket());
    }

    private static void handlePacket(Packet<?> packet) {
        if (packet.getClass().getSimpleName().startsWith("C")) {
            ++outBound;
        } else if (packet.getClass().getSimpleName().startsWith("S")) {
            ++inBound;
        }
        if (packet instanceof S32PacketConfirmTransaction && !PacketUtils.isInventoryAction(((S32PacketConfirmTransaction)packet).func_148890_d())) {
            ++transCount;
        }
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
        if (PacketUtils.mc.field_71439_g == null || PacketUtils.mc.field_71441_e == null) {
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

    public static void sendPacketNoEvent(Packet<INetHandlerPlayServer> packet) {
        packets.add(packet);
        mc.func_147114_u().func_147297_a(packet);
    }

    public static boolean handleSendPacket(Packet<?> packet) {
        if (packets.contains(packet)) {
            packets.remove(packet);
            PacketUtils.handlePacket(packet);
            return true;
        }
        return false;
    }

    @Override
    public boolean handleEvents() {
        return true;
    }

    static {
        outBound = 0;
        avgOutBound = 0;
        packets = new ArrayList();
        packetTimer = new MSTimer();
        wdTimer = new MSTimer();
        transCount = 0;
        wdVL = 0;
    }
}

