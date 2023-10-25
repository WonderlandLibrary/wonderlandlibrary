/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.network;

import baritone.events.events.network.EventPostSendPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.ThreadQuickExitException;
import net.minecraft.util.IThreadListener;
import org.celestial.client.event.EventManager;
import org.celestial.client.event.events.impl.packet.EventReceivePacket;

public class PacketThreadUtil {
    public static <T extends INetHandler> void checkThreadAndEnqueue(final Packet<T> packetIn, final T processor, IThreadListener scheduler) throws ThreadQuickExitException {
        if (!scheduler.isCallingFromMinecraftThread()) {
            scheduler.addScheduledTask(new Runnable(){

                @Override
                public void run() {
                    EventReceivePacket eventReceivePacket = new EventReceivePacket(packetIn);
                    EventManager.call(eventReceivePacket);
                    if (!eventReceivePacket.isCancelled()) {
                        packetIn.processPacket(processor);
                    }
                    EventPostSendPacket eventPostSendPacket = new EventPostSendPacket(packetIn);
                    EventManager.call(eventPostSendPacket);
                }
            });
            throw ThreadQuickExitException.INSTANCE;
        }
    }
}

